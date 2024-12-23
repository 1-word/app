package com.numo.wordapp.dto.word.search;

import com.numo.wordapp.entity.word.type.ReadType;

public record SearchWordRequestDto(
        Long folderId,
        Long lastWordId,
        String memorization,
        String language,
        ReadType readType
) {
}
