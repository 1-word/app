package com.numo.domain.sentence.dto;


import com.numo.domain.word.Word;

public record CreateWordDailySentenceDto(
        Word word,
        String matchedWord
) {
}
