package com.numo.api.domain.wordbook.dto;

import com.numo.domain.wordbook.WordBookRole;

public record WordBookRoleRequestDto(
        Long userId,
        WordBookRole role
) {
}
