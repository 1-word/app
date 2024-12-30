package com.numo.api.domain.wordbook.word.dto.search;

import com.numo.domain.word.type.ReadType;

public record SearchWordRequestDto(
        Long folderId,
        Long lastWordId,
        String memorization,
        String language,
        ReadType readType
) {
}
