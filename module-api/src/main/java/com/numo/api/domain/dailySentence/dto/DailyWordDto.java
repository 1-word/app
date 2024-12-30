package com.numo.api.domain.dailySentence.dto;

import lombok.Builder;

@Builder
public record DailyWordDto(
        Long wordId,
        String word
) {
}
