package com.numo.wordapp.security.service;

import com.numo.wordapp.security.dto.UserDto;

public interface UserService {
    public UserDto.Response getMemberInfo(String username);
    public UserDto.Response getMyInfo();

}
