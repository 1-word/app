package com.numo.wordapp.security.conf;

import com.numo.wordapp.security.jwt.JwtAccessDeniedHandler;
import com.numo.wordapp.security.jwt.JwtAuthenticationEntryPoint;
import com.numo.wordapp.security.jwt.JwtFilter;
import com.numo.wordapp.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        http.exceptionHandling(handlingConfigurer -> {
            handlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
            handlingConfigurer.accessDeniedHandler(jwtAccessDeniedHandler);
        });

        http.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/login/**").permitAll();
            authorize.requestMatchers("/signup/**").permitAll();
            authorize.requestMatchers("/reissue/**").permitAll();
            authorize.requestMatchers("/api-spec/**", "/v3/**").permitAll();
            authorize.requestMatchers("word/**").hasRole("ADMIN");
            authorize.anyRequest().authenticated();
        });

        http.addFilterAt(new JwtFilter(tokenProvider), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        String[] list = new String[] {"http://localhost:8080"
                                , "http://localhost:3000"
                                , "http://144.24.78.52:3000"
                                , "http://localhost:8088"
                                , "http://app:8088"
                                , "https://localhost:3000"
                                , "https://144.24.78.52:3000"
                                , "https://www.wordbook.kro.kr/"};
        config.setAllowCredentials(true);   //내 서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
//        config.addAllowedOriginPattern("*");   //모든 ip에 응답 허용
//        config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:3000", "http://144.24.78.52:3000", "http://localhost:8088", "http://app:8088"));  //해당 ip cors 허용
        config.setAllowedOriginPatterns(Arrays.asList(list));
        config.addAllowedHeader("*");   // 모든 header에 응답 허용
        config.addAllowedMethod("*");   //모든 post, get, put, delete, patch 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
