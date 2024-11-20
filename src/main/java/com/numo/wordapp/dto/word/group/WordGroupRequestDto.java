package com.numo.wordapp.dto.word.group;

import com.numo.wordapp.entity.word.detail.WordGroup;
import lombok.Builder;

@Builder
public record WordGroupRequestDto(
        String name,
        String description
) {
    public WordGroup toEntity() {
        return WordGroup.builder()
                .name(name)
                .description(description)
                .build();
    }
}
