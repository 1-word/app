package com.numo.api.security.oauth2;

import com.numo.api.conf.PropertyConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CommonLoginFailureHandler implements AuthenticationFailureHandler {
    private final PropertyConfig propertyConfig;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        String clientHost = propertyConfig.getClientHost();
        response.sendRedirect(clientHost + "/error?code=" + error.getErrorCode());
    }
}
