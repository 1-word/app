package com.numo.wordapp.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZTION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    //토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        //request에서 토큰을 받아옴
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        log.info("requestURI: {}", requestURI);

        //유효성 검증
        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
            //정상이면 SecurityContext에 저장
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            //SecurityContext에 Authentication객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}'인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            String id = tokenProvider.parseClaims(jwt).getSubject();
            log.info("login: "+id);
        }else{
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        log.info("jwtFilter 실행");

        chain.doFilter(request, response);
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
