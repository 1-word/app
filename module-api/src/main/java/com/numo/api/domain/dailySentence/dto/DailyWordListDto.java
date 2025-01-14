package com.numo.api.domain.dailySentence.dto;

import com.numo.api.domain.dailySentence.dto.wordDailySentence.DailyWordDetailDto;

import java.util.List;

public record DailyWordListDto(
        List<DailyWordDto> wordDtos,
        List<DailyWordDetailDto> detailDtos
) {
}
