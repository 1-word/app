package com.numo.api.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;


@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing  //JPA Auditing 활성화
@EntityScan(basePackages = "com.numo.domain")
public class DatabaseConfig {
    private final GlobalPropertySource globalPropertySource;

    @Bean
    public DataSource dataSource(){
        return DataSourceBuilder
                .create()
                .url(globalPropertySource.getUrl())
                .driverClassName(globalPropertySource.getDriverClassName())
                .username(globalPropertySource.getUsername())
                .password(globalPropertySource.getPassword())
                .build();
    }
}
