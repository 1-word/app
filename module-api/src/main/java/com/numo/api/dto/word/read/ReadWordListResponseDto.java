package com.numo.api.dto.word.read;

import com.numo.api.dto.page.PageDto;

import java.util.List;

public record ReadWordListResponseDto(
        List<ReadWordResponseDto> words,
        PageDto page
) {
}
