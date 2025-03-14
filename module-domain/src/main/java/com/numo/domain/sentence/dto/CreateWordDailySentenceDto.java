package com.numo.domain.sentence.dto;


import com.numo.domain.wordbook.word.Word;

public record CreateWordDailySentenceDto(
        Word word,
        String matchedWord
) {
}
