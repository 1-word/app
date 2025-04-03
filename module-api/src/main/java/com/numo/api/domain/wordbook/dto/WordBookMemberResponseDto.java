package com.numo.api.domain.wordbook.dto;

import com.numo.domain.wordbook.WordBookRole;

public record WordBookMemberResponseDto(
        Long id,
        Long userId,
        String profileImagePath,
        String nickname,
        WordBookRole role
) {
}
