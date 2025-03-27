package com.numo.api.domain.wordbook.word.dto.search;

import com.numo.domain.wordbook.type.SortType;

public record SearchWordRequestDto(
        @Deprecated
        Long folderId,
        Long wordBookId,
        Long lastWordId,
        String memorization,
        String language,
        SortType sort,
        String seed
) {
    public SearchWordRequestDto {
        if (folderId != null) {
            wordBookId = folderId;
        }
    }
}
