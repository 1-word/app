package com.numo.api.domain.dailySentence.dto.wordDailySentence;

public record WordDailySentenceDto(
        Long wordId,
        Long wordBookId,
        String matchedWord
) {
}
