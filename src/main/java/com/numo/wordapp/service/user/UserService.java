package com.numo.wordapp.service.user;

import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.dto.user.LoginDto;
import com.numo.wordapp.dto.user.TokenDto;
import com.numo.wordapp.dto.user.UserDto;
import com.numo.wordapp.security.jwt.TokenProvider;
import com.numo.wordapp.entity.user.Authority;
import com.numo.wordapp.entity.user.RefreshToken;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.repository.user.RefreshTokenRepository;
import com.numo.wordapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public TokenDto login(LoginDto loginDto){
        User user = userRepository.findOneWithAuthoritiesByUserId(loginDto.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.LoginIDFail));

        // 패스워드 일치 확인
        if (!passwordEncoder.matches(loginDto.password(), user.getPassword()))
            throw new CustomException(ErrorCode.LoginPWFail);

        //해당 ID로된 토큰이 있는지 확인하고 있다면 update를 해줌
        //""은 찾지 못한 것을 뜻함.
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getUserId()).orElse(new RefreshToken("", ""));
        return createRefreshToken(user, refreshToken);
    }

    public TokenDto createRefreshToken(User user, RefreshToken refreshToken){
        RefreshToken rtk = null;

        //토큰 생성
        // auth 는 user.getAuthNameList()로 가져올 수 있음
        // createTokenDto(아이디, 권한목록): 실제로 토큰 생성
        // 토큰 생성 후 데이터 베이스에 저장해야 함
        TokenDto tokenDto = tokenProvider.createTokenDto(user.getUserId(), user.getAuthNameList());

        //데이터 베이스 연동
        if (refreshToken.getUserId().isEmpty()) {
            rtk = RefreshToken.builder()
                    .user_id(user.getUserId())
                    .token(tokenDto.accessToken())
                    .build();
        } else {
            rtk = refreshToken;
            rtk.updateToken(tokenDto.refreshToken());
        }
        refreshTokenRepository.save(rtk);
        return tokenDto;
    }

    @Transactional
    public User signup(UserDto.Request userDto){
        //Username 확인
        if (userRepository.findOneWithAuthoritiesByUserId(userDto.getUser_id()).orElse(null) != null) {
            throw new CustomException(ErrorCode.DuplicatedIdFound);
        }

        //권한은 ROLE_USER
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .userId((userDto.getUser_id()))
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    public User getUserWithAuthorities(String userId){
        return userRepository.findOneWithAuthoritiesByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.DataNotFound));
    }

    public TokenDto reissue(TokenDto tokenDto){
        // 만료된 refresh token 에러
        if (!tokenProvider.validateToken(tokenDto.refreshToken())){
            throw new CustomException(ErrorCode.ExpiredToken);
        }

        // AccessToken 에서 username(Pk) 가져오기
        String accessToken = tokenDto.accessToken();
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        System.out.println("AccessToken: " + authentication.getName());

        // userPk로 유저 검색
        User user = userRepository.findById(authentication.getName())
                        .orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));

        // refreshToken 검색
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getUserId())
                        .orElseThrow(() -> new CustomException(ErrorCode.RefreshTokenNotFound));

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenDto.refreshToken()))
            throw new CustomException(ErrorCode.RefreshTokenNotEqual);

        return createRefreshToken(user, refreshToken);
    }
}
