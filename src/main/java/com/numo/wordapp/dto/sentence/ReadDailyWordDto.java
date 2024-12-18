package com.numo.wordapp.dto.sentence;

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
