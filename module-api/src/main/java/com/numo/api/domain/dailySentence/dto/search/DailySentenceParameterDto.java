package com.numo.api.domain.dailySentence.dto.search;

public record DailySentenceParameterDto(
        Integer year,
        Integer month,
        Integer day,
        Integer week
) {
}
