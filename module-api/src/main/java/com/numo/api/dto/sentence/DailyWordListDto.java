package com.numo.api.dto.sentence;

import com.numo.api.dto.sentence.wordDailySentence.DailyWordDetailDto;

import java.util.List;

public record DailyWordListDto(
        List<DailyWordDto> wordDtos,
        List<DailyWordDetailDto> detailDtos
) {
}
