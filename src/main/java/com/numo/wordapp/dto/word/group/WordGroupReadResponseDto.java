package com.numo.wordapp.dto.word.group;

import com.numo.wordapp.entity.word.detail.WordGroup;
import lombok.Builder;

@Builder
public record WordGroupReadResponseDto(
        WordGroupResponseDto wordGroup
) {
    public static WordGroupReadResponseDto of(WordGroup wordGroup) {
        WordGroupResponseDto wordGroupRes = WordGroupResponseDto.builder()
                .wordGroupId(wordGroup.getWordGroupId())
                .name(wordGroup.getName())
                .description(wordGroup.getDescription())
                .build();

        return WordGroupReadResponseDto.builder()
                .wordGroup(wordGroupRes)
                .build();
    }
}
