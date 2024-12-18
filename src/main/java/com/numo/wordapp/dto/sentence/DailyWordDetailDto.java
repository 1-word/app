package com.numo.wordapp.dto.sentence;

public record DailyWordDetailDto(
        Long wordDetailId,
        Long wordId,
        String title,
        String content
) {
}
