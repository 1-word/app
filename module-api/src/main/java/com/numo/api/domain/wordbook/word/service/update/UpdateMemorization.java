package com.numo.api.domain.wordbook.word.service.update;

import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;

public class UpdateMemorization implements UpdateWord {
    @Override
    public Word update(UpdateWordDto dto, Word word) {
        word.updateMemorization(dto.memorization());
        WordBook wordbook = word.getWordbook();
        String memorization = word.getMemorization();
        wordbook.updateCount(memorization);
        return word;
    }
}
