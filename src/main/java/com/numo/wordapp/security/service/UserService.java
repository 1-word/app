package com.numo.wordapp.security.service;

import com.numo.wordapp.security.dto.UserDto;
import com.numo.wordapp.security.model.User;

import java.util.Optional;

public interface UserService {
    User signup(UserDto.Request userDto);
    Optional<User> getUserWithAuthorities(String username);
    Optional<User> getMyUserWithAuthorities();
}
