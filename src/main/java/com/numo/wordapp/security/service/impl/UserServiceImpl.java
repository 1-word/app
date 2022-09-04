package com.numo.wordapp.security.service.impl;

import com.numo.wordapp.security.dto.UserDto;
import com.numo.wordapp.security.model.Authority;
import com.numo.wordapp.security.model.User;
import com.numo.wordapp.security.repository.UserRepository;
import com.numo.wordapp.security.service.UserService;
import com.numo.wordapp.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User signup(UserDto.Request userDto){
        //Username 확인
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        //권한은 ROLE_USER
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username((userDto.getUsername()))
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    //username을 기준으로 정보를 가져옴
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username){
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    //SecurityContext에 저장된 username의 정보만 가져옴
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities(){
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}
