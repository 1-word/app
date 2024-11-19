package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.page.PageDto;

public record SearchWordRequestDto(
        Long folderId,
        PageDto page,
        Long lastWordId,
        String memorization,
        String language
) {
}
