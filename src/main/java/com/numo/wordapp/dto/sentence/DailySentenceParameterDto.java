package com.numo.wordapp.dto.sentence;

public record DailySentenceParameterDto(
        Integer year,
        Integer month,
        Integer day,
        Integer week
) {
}
