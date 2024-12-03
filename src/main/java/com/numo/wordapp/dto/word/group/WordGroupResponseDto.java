package com.numo.wordapp.dto.word.group;

import com.numo.wordapp.entity.word.detail.WordGroup;
import lombok.Builder;

@Builder
public record WordGroupResponseDto(
        Long wordGroupId,
        String name,
        String description,
        boolean isDefaultGroup
) {
    public static WordGroupResponseDto of(WordGroup wordGroup) {
        return WordGroupResponseDto.builder()
                .wordGroupId(wordGroup.getWordGroupId())
                .name(wordGroup.getName())
                .description(wordGroup.getDescription())
                .isDefaultGroup(wordGroup.isDefaultGroup())
                .build();
    }
}
