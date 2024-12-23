package com.numo.api.dto.sentence;

import lombok.Builder;

@Builder
public record DailyWordDto(
        Long wordId,
        String word
) {
}
