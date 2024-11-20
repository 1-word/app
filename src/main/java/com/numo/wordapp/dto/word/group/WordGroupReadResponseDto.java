package com.numo.wordapp.dto.word.group;

import com.numo.wordapp.dto.word.detail.WordDetailResponseDto;
import com.numo.wordapp.entity.word.detail.WordGroup;
import lombok.Builder;

import java.util.List;

@Builder
public record WordGroupReadResponseDto(
        WordGroupResponseDto wordGroup,
        List<WordDetailResponseDto> details
) {
    public static WordGroupReadResponseDto of(WordGroup wordGroup) {
        WordGroupResponseDto wordGroupRes = WordGroupResponseDto.builder()
                .wordGroupId(wordGroup.getWordGroupId())
                .name(wordGroup.getName())
                .description(wordGroup.getDescription())
                .build();

        List<WordDetailResponseDto> detailsRes = wordGroup.getDetails().stream()
                .map(WordDetailResponseDto::of).toList();

        return WordGroupReadResponseDto.builder()
                .wordGroup(wordGroupRes)
                .details(detailsRes)
                .build();
    }
}
