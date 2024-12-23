package com.numo.domain.user.dto;

import lombok.Builder;

@Builder
public record UpdateUserDto(
        String nickname,
        String profileImagePath
) {
}
