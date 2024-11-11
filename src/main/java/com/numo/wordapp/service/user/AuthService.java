package com.numo.wordapp.service.user;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.user.LoginDto;
import com.numo.wordapp.dto.user.TokenDto;
import com.numo.wordapp.dto.user.UserDto;
import com.numo.wordapp.entity.user.RefreshToken;
import com.numo.wordapp.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenDto login(LoginDto loginDto) {
        UserDto user = userService.findUserAndCheckPassword(loginDto.email(), loginDto.password());
        // 토큰 발급
        TokenDto tokenDto = tokenProvider.createTokenDto(user.email(), user.authorities());
        refreshTokenService.saveOrUpdateToken(user.userId(), tokenDto.refreshToken());
        return tokenDto;
    }

    public TokenDto reissue(String refreshToken) {
        // 만료된 refresh token 에러
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        // refreshToken 검색
        RefreshToken oldRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);

        // 리프레시 토큰 불일치 에러
        if (!oldRefreshToken.getToken().equals(refreshToken))
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);

        UserDto user = userService.findByUserId(oldRefreshToken.getUserId());
        TokenDto tokenDto = tokenProvider.createTokenDto(user.email(), user.authorities());
        refreshTokenService.saveOrUpdateToken(user.userId(), tokenDto.refreshToken());

        return tokenDto;
    }

    public void logout(Long userId) {
        refreshTokenService.deleteToken(userId);
    }
}
