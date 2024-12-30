package com.numo.api.domain.wordbook.group.dto;

import lombok.Builder;

@Builder
public record WordGroupDetailDto(
        Long wordId,
        Long wordGroupId,
        Long wordDetailId,
        String name,
        String title,
        String content
) {
}
