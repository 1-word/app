package com.numo.api.dto.sentence.wordDailySentence;

public record DailyWordDetailDto(
        Long wordDetailId,
        Long wordId,
        String title,
        String content
) {
}
