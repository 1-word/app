package com.numo.wordapp.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {
    private SecurityUtil(){

    }
    // SecurityContext의 Authentication 객체를 이용해 username 리턴
    public static Optional<String> getCurrentUsername(){
        // SecurityContext에 Authentication 객체가 저장되는 시점은
        // JwtFilter.class의 doFilter()에서 Request가 들어올 때 SecurityContext에 Authentication 객체 저장
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null){
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails){
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String){
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
}
