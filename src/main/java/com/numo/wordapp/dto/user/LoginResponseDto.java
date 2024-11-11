package com.numo.wordapp.dto.user;

import com.numo.wordapp.entity.user.User;
import lombok.Builder;

@Builder
public record LoginResponseDto(
        Long userId,
        String email,
        String username
) {
    public static LoginResponseDto of(User user) {
        return LoginResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getEmail())
                .build();
    }
}
