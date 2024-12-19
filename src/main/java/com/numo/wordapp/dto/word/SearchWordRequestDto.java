package com.numo.wordapp.dto.word;

import com.numo.wordapp.entity.word.ReadType;

public record SearchWordRequestDto(
        Long folderId,
        Long lastWordId,
        String memorization,
        String language,
        ReadType readType
) {
}
