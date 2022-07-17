package com.numo.wordapp.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //CORS 설정
        registry.addMapping("/**")  //모든 url패턴
                .allowedMethods("*")    //모든 method
                .allowedOrigins("http://localhost:3000"
                        , "http://localhost:8080"
                        , "http://144.24.78.52:3000");  //해당하는 url
    }

}
