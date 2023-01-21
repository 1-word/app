package com.numo.wordapp.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.comm.advice.exception.TokenCException;
import com.numo.wordapp.model.CommonResult;
import com.numo.wordapp.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 유효한 자격증명을 제공하지 않고 접근하려 할 때 401 에러를 리턴할 클래스
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseService responseService;

    public JwtAuthenticationEntryPoint(ResponseService responseService){
        this.responseService = responseService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        responseService.setResponseError(response, HttpStatus.UNAUTHORIZED.value(), ErrorCode.OperationNotAuthorized);
    }
}
