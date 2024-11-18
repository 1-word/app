package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.word.detail.WordDetailRequestDto;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateWordDto(
        Long wordId,
        Long folderId,
        String word,
        String mean,
        String read,
        String memo,
        String memorization,
        List<WordDetailRequestDto> details
) {
}
