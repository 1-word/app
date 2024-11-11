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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//인가 에러
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("403 forbidden, {}", request.getRequestURI());
        HttpStatus status = HttpStatus.FORBIDDEN;
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        CommonResult.builder()
                                .code(status.value())
                                .success(false)
                                .msg(ErrorCode.UNRECOGNIZED_ROLE.getDescription())
                                .build()
                )
        );
    }
}
