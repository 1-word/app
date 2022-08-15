package com.numo.wordapp.security.service.impl;

import com.numo.wordapp.security.dto.UserDto;
import com.numo.wordapp.security.repository.UserRepository;
import com.numo.wordapp.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    /*private UserRepository userRepository;

    @Override
    public UserDto.Response getMemberInfo(String username){
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map((UserDto.Response::of))
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    @Override
    public UserDto.Response getMyInfo() {
        return userRepository.findById(Security)
    }
*/
}
