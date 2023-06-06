package com.numo.wordapp.security.conf;

import com.numo.wordapp.security.jwt.JwtAccessDeniedHandler;
import com.numo.wordapp.security.jwt.JwtAuthenticationEntryPoint;
import com.numo.wordapp.security.jwt.JwtSecurityConfig;
import com.numo.wordapp.security.jwt.TokenProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //@PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler){
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //시큐리티 ignore 설정
    @Override
    public void configure(WebSecurity web){
        web.ignoring()
                .antMatchers("/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()  //토큰을 사용하므로 csrf 설정 disable
                .cors().configurationSource(corsConfigurationSource())  //cors필터 등록

                .and()
                .exceptionHandling() //에러 핸들링
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용X

                .and()
                //.addFilter(corsFilter)  //@CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()// httpServletRequest를 사용하는 요청들에 대한 접근제한 설정
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/api/signup/**").permitAll()
                .antMatchers("word/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                /*.antMatchers("api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")*/
                .anyRequest().authenticated() //나머지 요청들은 모두 인증되어야 함
//                .anyRequest().permitAll()  //나머지 요청들은 인증 없음

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));   //filter 등록
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        String[] list = new String[] {"http://localhost:8080"
                                , "http://localhost:3000"
                                , "http://144.24.78.52:3000"
                                , "http://localhost:8088"
                                , "http://app:8088"};
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
