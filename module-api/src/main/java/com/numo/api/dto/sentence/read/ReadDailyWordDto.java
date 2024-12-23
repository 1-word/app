package com.numo.api.dto.sentence.read;

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
