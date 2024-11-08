package com.numo.wordapp.test.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

// 설정 파일 주입
@TestPropertySource(locations = "classpath:application-test.properties")
// 구동 환경 설정
@SpringBootTest(properties = "spring.profiles.active:test")
@Transactional
public class UserServiceTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("패스워드 생성")
    @Test
    public void Test(){
        System.out.println(passwordEncoder.encode("test"));
    }
}
