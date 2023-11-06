package com.numo.wordapp.security.service.impl;

import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.comm.advice.exception.TokenCException;
import com.numo.wordapp.comm.advice.exception.CustomException;
import com.numo.wordapp.security.dto.LoginDto;
import com.numo.wordapp.security.dto.TokenDto;
import com.numo.wordapp.security.dto.UserDto;
import com.numo.wordapp.security.jwt.TokenProvider;
import com.numo.wordapp.security.model.Authority;
import com.numo.wordapp.security.model.RefreshToken;
import com.numo.wordapp.security.model.User;
import com.numo.wordapp.security.repository.RefreshTokenRepository;
import com.numo.wordapp.security.repository.UserRepository;
import com.numo.wordapp.security.service.UserService;
import com.numo.wordapp.security.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordEncoder passwordEncoder,
                           TokenProvider tokenProvider){
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public TokenDto.response login(LoginDto loginDto){
        System.out.println("ee");
        User user = userRepository.findOneWithAuthoritiesByUserId(loginDto.getUser_id())
                .orElseThrow(() -> new CustomException(ErrorCode.LoginIDFail));

        // 패스워드 일치 확인
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.LoginPWFail);

        //해당 ID로된 토큰이 있는지 확인하고 있다면 update를 해줌
        //""은 찾지 못한 것을 뜻함.
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getUserId()).orElse( new RefreshToken("", ""));
        TokenDto.response tokenDto = createRefreshToken(user, refreshToken);

        return tokenDto;
    }


    public TokenDto.response createRefreshToken(User user, RefreshToken refreshToken){
        RefreshToken rtk = new RefreshToken();

        //토큰 생성
        // auth 는 user.getAuthNameList()로 가져올 수 있음
        // createTokenDto(아이디, 권한목록): 실제로 토큰 생성
        // 토큰 생성 후 데이터 베이스에 저장해야 함
        TokenDto.response tokenDto = tokenProvider.createTokenDto(user.getUserId(), user.getAuthNameList());

        //데이터 베이스 연동
        if (refreshToken.getUserId() == "") {
            rtk = RefreshToken.builder()
                    .user_id(user.getUserId())
                    .token(tokenDto.getRefreshToken())
                    .build();
        }else{
            rtk = refreshToken;
            rtk.updateToken(tokenDto.getRefreshToken());
        }
        refreshTokenRepository.save(rtk);
        return tokenDto;
    }

    @Override
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

    //username을 기준으로 정보를 가져옴
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username){
        return userRepository.findOneWithAuthoritiesByUserId(username);
    }

    //SecurityContext에 저장된 username의 정보만 가져옴
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities(){
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUserId);
    }

    @Override
    public TokenDto.response reissue(TokenDto.request tokenDto){
        // 만료된 refresh token 에러
        if (!tokenProvider.validateToken(tokenDto.getRefreshToken())){
            throw new CustomException(ErrorCode.ExpiredToken);
        }

        // AccessToken 에서 username(Pk) 가져오기
        String accessToken = tokenDto.getAccessToken();
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        System.out.println("AccessToken: " + authentication.getName());

        // userPk로 유저 검색
        User user = userRepository.findById(authentication.getName())
                        .orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));

        // refreshToken 검색
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getUserId())
                        .orElseThrow(() -> new CustomException(ErrorCode.RefreshTokenNotFound));

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenDto.getRefreshToken()))
            throw new CustomException(ErrorCode.RefreshTokenNotEqual);

        return createRefreshToken(user, refreshToken);
    }
}
