package com.numo.wordapp.security.jwt;

import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.comm.advice.exception.TokenCException;
import com.numo.wordapp.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//인가 에러
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseService responseService;

    public JwtAccessDeniedHandler(ResponseService responseService){
        this.responseService = responseService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        responseService.setResponseError(response, HttpStatus.FORBIDDEN.value(), ErrorCode.UnrecongizedRole);
    }
}
