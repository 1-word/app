package com.numo.domain.wordbook.dto;

import lombok.Builder;

@Builder
public record WordBookUpdateDto(
        String name,
        String memo,
        String color,
        String background
) {
}
