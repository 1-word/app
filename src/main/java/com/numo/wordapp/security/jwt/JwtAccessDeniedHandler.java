package com.numo.wordapp.security.jwt;

import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.service.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

//인가 에러
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseService responseService;

    public JwtAccessDeniedHandler(ResponseService responseService){
        this.responseService = responseService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("403 forbidden, {}", request.getRequestURI());
        responseService.setResponseError(response, HttpStatus.FORBIDDEN.value(), ErrorCode.UnrecognizedRole);
    }
}
