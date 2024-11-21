package com.numo.wordapp.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numo.wordapp.dto.user.TokenDto;
import com.numo.wordapp.security.jwt.TokenProvider;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.user.RefreshTokenService;
import jakarta.servlet.ServletException;
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
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(userDetails.getUsername(), userDetails.getAuthorityList());
        refreshTokenService.saveOrUpdateToken(userDetails.getUserId(), tokenDto.refreshToken());

        ObjectMapper mapper = new ObjectMapper();
        String res = mapper.writeValueAsString(tokenDto);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(res);
    }
}
