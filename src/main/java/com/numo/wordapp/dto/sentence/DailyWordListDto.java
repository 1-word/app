package com.numo.wordapp.dto.sentence;

import com.numo.wordapp.dto.sentence.wordDailySentence.DailyWordDetailDto;

import java.util.List;

public record DailyWordListDto(
        List<DailyWordDto> wordDtos,
        List<DailyWordDetailDto> detailDtos
) {
}
