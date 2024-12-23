package com.numo.api.security.oauth2;

import com.numo.api.conf.PropertyConfig;
import com.numo.api.dto.user.TokenDto;
import com.numo.api.security.jwt.TokenProvider;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.service.user.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CommonLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final PropertyConfig propertyConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(userDetails.getUsername(), userDetails.getAuthorityList());
        refreshTokenService.saveOrUpdateToken(userDetails.getUserId(), tokenDto.refreshToken());

        Cookie accessToken = new Cookie("accessToken", tokenDto.accessToken());
        Cookie refreshToken = new Cookie("refreshToken", tokenDto.refreshToken());

        accessToken.setPath("/");
        refreshToken.setPath("/");

        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        String clientHost = propertyConfig.getClientHost();
        response.sendRedirect(clientHost+ "/oauth/callback");
    }
}
