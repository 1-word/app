package com.numo.wordapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class WordAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordAppApplication.class, args);
    }

}
