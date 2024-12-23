package com.numo.wordapp.dto.word.read;

import com.numo.wordapp.dto.page.PageDto;

import java.util.List;

public record ReadWordListResponseDto(
        List<ReadWordResponseDto> words,
        PageDto page
) {
}
