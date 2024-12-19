package com.numo.wordapp.dto.sentence;

import com.numo.wordapp.entity.word.Word;

public record CreateWordDailySentenceDto(
        Word word,
        String matchedWord
) {
}
