package com.numo.wordapp.service.word;

import com.numo.wordapp.dto.word.UpdateWordDto;
import com.numo.wordapp.entity.word.Word;

public class UpdateMemorization implements UpdateWord {
    @Override
    public Word update(UpdateWordDto dto, Word word) {
        word.updateMemorization(dto.memorization());
        return word;
    }
}