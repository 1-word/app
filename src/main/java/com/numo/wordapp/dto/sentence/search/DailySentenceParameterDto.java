package com.numo.wordapp.dto.sentence.search;

public record DailySentenceParameterDto(
        Integer year,
        Integer month,
        Integer day,
        Integer week
) {
}
