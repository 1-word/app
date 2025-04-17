package com.numo.api.domain.user.dto;

public record SearchUserDto(
        Long userId,
        String nickname,
        String email,
        String profileImagePath
) {
}
