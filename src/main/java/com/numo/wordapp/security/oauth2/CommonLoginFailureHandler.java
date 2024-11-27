package com.numo.wordapp.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numo.wordapp.comm.response.CommonResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CommonLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        CommonResult result = new CommonResult(
                false,
                401,
                "소셜 로그인 중 에러가 발생했습니다.",
                null
        );
        String res = mapper.writeValueAsString(result);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(res);
    }
}
