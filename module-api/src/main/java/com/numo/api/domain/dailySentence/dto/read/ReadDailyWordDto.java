package com.numo.api.domain.dailySentence.dto.read;

import lombok.Builder;

@Builder
public record ReadDailyWordDto(
        Long wordDailyWordId,
        Long wordId,
        String word,
        String mean,
        Long folderId,
        String folderName
) {
}
