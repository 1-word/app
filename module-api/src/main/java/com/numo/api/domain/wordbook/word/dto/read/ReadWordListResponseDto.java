package com.numo.api.domain.wordbook.word.dto.read;

import com.numo.api.global.comm.page.PageDto;

import java.util.List;

public record ReadWordListResponseDto(
        List<ReadWordResponseDto> words,
        PageDto page
) {
}
