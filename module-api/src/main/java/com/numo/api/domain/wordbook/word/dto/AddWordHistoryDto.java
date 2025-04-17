package com.numo.api.domain.wordbook.word.dto;

import com.numo.domain.user.User;
import com.numo.domain.wordbook.word.WordHistory;

public record AddWordHistoryDto(
        WordHistory.Operation operation,
        User modifiedBy,
        Long wordBookId,
        Long wordId,
        String beforeData,
        String afterData
) {
    public WordHistory toEntity() {
        return WordHistory.builder()
                .operation(operation)
                .wordBookId(wordBookId)
                .wordId(wordId)
                .modifiedBy(modifiedBy)
                .beforeData(beforeData)
                .afterData(afterData)
                .build();
    }
}
