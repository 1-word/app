package com.numo.api.domain.wordbook.word.dto.search;

import com.numo.domain.word.type.SortType;

public record SearchWordRequestDto(
        Long folderId,
        Long lastWordId,
        String memorization,
        String language,
        SortType sort,
        String seed
) {
}
