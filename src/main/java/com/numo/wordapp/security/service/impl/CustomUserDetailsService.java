package com.numo.wordapp.security.service.impl;

import com.numo.wordapp.security.model.User;
import com.numo.wordapp.security.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUserId(user_id)//db에서 유저정보와 권한정보 가져옴
                .map(user -> createUser(user_id, user))// 해당 정보로 userdetails.User 객체 생성
                .orElseThrow(() -> new UsernameNotFoundException(user_id + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String user_id, User user){
        if(!user.isActivated()){    //계정이 활성화 된 것만 가져옴
            throw new RuntimeException(user_id + " -> 활성화되어 있지 않습니다.");
        }

        //userdetails 권한 정보
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority((authority.getAuthorityName())))
                .collect(Collectors.toList());

        //userdetails.User 객체 생성 후 리턴
        return new org.springframework.security.core.userdetails.User(user.getUserId(),
                user.getPassword(),
                grantedAuthorities);
    }
}

