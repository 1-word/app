package com.numo.api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZTION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request에서 토큰을 받아옴
        String token = resolveToken(request);
        String requestURI = request.getRequestURI();

        log.debug("requestURI: {}", requestURI);

        //유효성 검증
        if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){
            //정상이면 SecurityContext에 저장
            Authentication authentication = tokenProvider.getAuthentication(token);
            //SecurityContext에 Authentication객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}'인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            String id = tokenProvider.parseClaims(token).getId();
            log.debug("login: " + id);
        }

        filterChain.doFilter(request, response);
    }

    // Request 헤더에서 토큰 정보를 꺼내옴
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZTION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
