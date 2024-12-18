package com.numo.wordapp.dto.sentence;

import java.util.List;

public record DailyWordListDto(
        List<DailyWordDto> wordDtos,
        List<DailyWordDetailDto> detailDtos
) {
}
