package com.numo.api.security.jwt;

import com.numo.api.dto.user.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final long tokenValidityInMilliseconds;
    private final long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L; // 14 day
    private Key key;
    private final UserDetailsService userDetailsService;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            UserDetailsService userDetailsService) {
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createTokenDto(String email, List<String> roles){
        Date now = new Date();
        String accessToken = Jwts.builder()
                .id(email)
                .claims()
                    .add(AUTHORITIES_KEY, roles)
                    .and()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidityInMilliseconds))
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .id(email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(key)
                .compact();
        
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getPayload(token);
        String email = claims.getId();

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token).getPayload();
    }

    public Claims parseClaims(String token){
        try {
            //jwt 토큰 복호화
            return getPayload(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            getPayload(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
