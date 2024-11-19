package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.page.PageDto;

import java.util.List;

public record ReadWordResponseDto(
        List<WordResponseDto> words,
        PageDto page
) {
}
