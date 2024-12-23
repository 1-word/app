package com.numo.api.dto.word.group;

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
