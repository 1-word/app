package com.numo.api.security.conf;

import com.numo.api.domain.user.service.RefreshTokenService;
import com.numo.api.global.conf.PropertyConfig;
import com.numo.api.security.handle.JwtAccessDeniedHandler;
import com.numo.api.security.handle.JwtAuthenticationEntryPoint;
import com.numo.api.security.jwt.JwtFilter;
import com.numo.api.security.jwt.TokenProvider;
import com.numo.api.security.oauth2.CommonLoginFailureHandler;
import com.numo.api.security.oauth2.CommonLoginSuccessHandler;
import com.numo.api.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    private final RefreshTokenService refreshTokenService;
    private final PropertyConfig propertyConfig;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/resources/**");
    }

    public CommonLoginSuccessHandler commonLoginSuccessHandler() {
        return new CommonLoginSuccessHandler(tokenProvider, refreshTokenService, propertyConfig);
    }

    public CommonLoginFailureHandler commonLoginFailureHandler() {
        return new CommonLoginFailureHandler(propertyConfig);
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    private JwtAccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2UserService<OAuth2UserRequest, OAuth2User> CustomOAuth2UserService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        http.exceptionHandling(handlingConfigurer -> {
            handlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
            handlingConfigurer.accessDeniedHandler(jwtAccessDeniedHandler());
        });

        http.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.POST,"/auth/**").permitAll();
            authorize.requestMatchers("/user/signup/**").permitAll();
            authorize.requestMatchers(HttpMethod.PUT, "user/pw/reset").permitAll();
            authorize.requestMatchers("/api-spec/**", "/v3/**").permitAll();
            authorize.requestMatchers("/files/images/**", "/files/upload/thumbnail").permitAll();
            authorize.requestMatchers("/oauth2/**", "/login/**").permitAll();
            authorize.requestMatchers("/resources/**").permitAll();
            authorize.requestMatchers(HttpMethod.GET,"/posts/**").permitAll();
            authorize.requestMatchers(HttpMethod.GET, "/share-rooms").permitAll();
            authorize.requestMatchers("/posts/**").hasRole("ADMIN");
            authorize.requestMatchers("/actuator/**").permitAll();
            authorize.anyRequest().authenticated();
        });

        http.oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                .authorizationEndpoint(config -> config.authorizationRequestRepository(authorizationRequestRepository()))
                .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
                        .baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(CustomOAuth2UserService))
                .successHandler(commonLoginSuccessHandler())
                .failureHandler(commonLoginFailureHandler())
        );

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
        String[] list = propertyConfig.getCorsUrl();
        config.setAllowCredentials(true);   //내 서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.setAllowedOriginPatterns(Arrays.asList(list));
        config.addAllowedHeader("*");   // 모든 header에 응답 허용
        config.addAllowedMethod("*");   //모든 post, get, put, delete, patch 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
