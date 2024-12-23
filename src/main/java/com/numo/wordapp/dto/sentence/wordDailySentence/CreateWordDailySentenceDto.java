package com.numo.wordapp.dto.sentence.wordDailySentence;

import com.numo.wordapp.entity.word.Word;

public record CreateWordDailySentenceDto(
        Word word,
        String matchedWord
) {
}
