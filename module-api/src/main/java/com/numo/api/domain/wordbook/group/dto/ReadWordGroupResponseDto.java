package com.numo.api.domain.wordbook.group.dto;

import com.numo.domain.wordbook.detail.WordGroup;
import lombok.Builder;

@Builder
public record ReadWordGroupResponseDto(
        WordGroupResponseDto wordGroup
) {
    public static ReadWordGroupResponseDto of(WordGroup wordGroup) {
        WordGroupResponseDto wordGroupRes = WordGroupResponseDto.builder()
                .wordGroupId(wordGroup.getWordGroupId())
                .name(wordGroup.getName())
                .description(wordGroup.getDescription())
                .build();

        return ReadWordGroupResponseDto.builder()
                .wordGroup(wordGroupRes)
                .build();
    }
}
