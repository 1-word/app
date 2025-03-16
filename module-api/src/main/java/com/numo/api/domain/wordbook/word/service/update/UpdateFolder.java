package com.numo.api.domain.wordbook.word.service.update;

import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;

public class UpdateFolder implements UpdateWord {
    @Override
    public Word update(UpdateWordDto dto, Word word) {
        word.setWordbook(dto.folderId());
        return word;
    }
}
