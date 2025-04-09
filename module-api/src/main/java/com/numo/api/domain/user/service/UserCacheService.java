package com.numo.api.domain.user.service;

import com.numo.api.domain.user.repository.UserRepository;
import com.numo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCacheService {
    private final UserRepository userRepository;

    @Cacheable(cacheNames = "user", key= "#p0")
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

}
