package com.numo.wordapp.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.comm.response.CommonResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 유효한 자격증명을 제공하지 않고 접근하려 할 때 401 에러를 리턴할 클래스
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("401 unauthorized, {}", request.getRequestURI());
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        CommonResult.builder()
                                .code(status.value())
                                .success(false)
                                .msg(ErrorCode.NOT_AUTHORIZED.getDescription())
                                .build()
                )
        );
    }
}
