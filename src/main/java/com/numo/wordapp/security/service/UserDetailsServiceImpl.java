package com.numo.wordapp.security.service;

import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUserId(userId)//db에서 유저정보와 권한정보 가져옴
                .map(user -> createUser(userId, user))// 해당 정보로 userdetails.User 객체 생성
                .orElseThrow(() -> new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUser(String userId, User user){
        if(!user.isActivated()){    //계정이 활성화 된 것만 가져옴
            throw new RuntimeException(userId + " -> 활성화되어 있지 않습니다.");
        }

        //userdetails.User 객체 생성 후 리턴
        return new UserDetailsImpl(user);
    }
}

