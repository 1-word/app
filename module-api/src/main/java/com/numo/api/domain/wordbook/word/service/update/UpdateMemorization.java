package com.numo.api.domain.wordbook.word.service.update;

import com.numo.domain.word.Word;
import com.numo.domain.word.dto.UpdateWordDto;

public class UpdateMemorization implements UpdateWord {
    @Override
    public Word update(UpdateWordDto dto, Word word) {
        word.updateMemorization(dto.memorization());
        return word;
    }
}
