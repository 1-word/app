package com.numo.wordapp.security.service.impl;

import antlr.Token;
import com.numo.wordapp.comm.advice.exception.TokenCException;
import com.numo.wordapp.comm.advice.exception.UserNotFoundCException;
import com.numo.wordapp.security.dto.LoginDto;
import com.numo.wordapp.security.dto.TokenDto;
import com.numo.wordapp.security.dto.UserDto;
import com.numo.wordapp.security.jwt.JwtFilter;
import com.numo.wordapp.security.jwt.TokenProvider;
import com.numo.wordapp.security.model.Authority;
import com.numo.wordapp.security.model.RefreshToken;
import com.numo.wordapp.security.model.User;
import com.numo.wordapp.security.repository.RefreshTokenRepository;
import com.numo.wordapp.security.repository.UserRepository;
import com.numo.wordapp.security.service.UserService;
import com.numo.wordapp.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    // refreshToken은 ID당 하나 필요하므로 동일한 ID로된 토큰이 저장되어있으면 업데이트 로직 추가해야함
    @Override
    public TokenDto.response login(LoginDto loginDto){
        User user = userRepository.findOneWithAuthoritiesByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundCException("해당하는 회원이 없습니다."));

        // 패스워드 일치 확인
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new UserNotFoundCException("비밀번호가 일치하지 않습니다.");

        Set<Authority> authoritys = user.getAuthorities();
        List<String> auth = new ArrayList<>();
        for (Authority authority : authoritys){
            auth.add(authority.getAuthorityName());
        }

        TokenDto.response tokenDto = tokenProvider.createTokenDto(user.getUserId(), auth);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getUserId())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Override
    @Transactional
    public User signup(UserDto.Request userDto){
        //Username 확인
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        //권한은 ROLE_USER
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username((userDto.getUsername()))
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    //username을 기준으로 정보를 가져옴
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username){
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    //SecurityContext에 저장된 username의 정보만 가져옴
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities(){
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    @Override
    public TokenDto.response reissue(TokenDto.request tokenDto){
        // 만료된 refresh token 에러
        if (!tokenProvider.validateToken(tokenDto.getRefreshToken())){
            throw new TokenCException("토큰 정보가 알맞지 않습니다.");
        }

        // AccessToken 에서 username(Pk) 가져오기
        String accessToken = tokenDto.getAccessToken();
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        // userPk로 유저 검색
        User user = userRepository.findById(Long.parseLong(authentication.getName()))
                        .orElseThrow(() -> new UserNotFoundCException("해당하는 유저가 없습니다."));

        // refreshToken 검색
        RefreshToken refreshToken = refreshTokenRepository.findByKey(user.getUserId())
                        .orElseThrow(() -> new TokenCException("저장된 토큰 정보가 없습니다."));

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenDto.getRefreshToken()))
            throw new TokenCException("토큰 정보가 일치하지 않습니다.");

        Set<Authority> authoritys = user.getAuthorities();
        List<String> auth = new ArrayList<>();
        for (Authority authority : authoritys){
            auth.add(authority.getAuthorityName());
        }
        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto.response newCreatedToken = tokenProvider.createTokenDto(user.getUserId(), auth);
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        refreshTokenRepository.save(updateRefreshToken);

        return newCreatedToken;
    }
}
