package com.numo.api.domain.dailySentence.dto.wordDailySentence;

public record DailyWordDetailDto(
        Long wordDetailId,
        Long wordId,
        String title,
        String content
) {
}
