package com.numo.wordapp.conf.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        // 개발환경과 실제 환경 모두 동작하게 하기 위해 지정
        // ignoreResourceNotFound 옵션을 이용하여 파일이 없을 때도 정상 동작하도록함
        @PropertySource(value = "file:C:/Users/Hyun/dev/config.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:${user.home}/env/config.properties", ignoreResourceNotFound = true)
})

@Getter
public class GlobalPropertySource {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
}
