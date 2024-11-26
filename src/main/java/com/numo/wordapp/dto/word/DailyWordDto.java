package com.numo.wordapp.dto.word;

import lombok.Builder;

@Builder
public record DailyWordDto(
        Long wordId,
        String word,
        String mean,
        Long folderId,
        String folderName
) {
}
