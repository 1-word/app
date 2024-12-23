package com.numo.wordapp.service.word.update;

import com.numo.wordapp.dto.word.UpdateWordDto;
import com.numo.wordapp.entity.word.Word;

public class UpdateFolder implements UpdateWord {
    @Override
    public Word update(UpdateWordDto dto, Word word) {
        word.setFolder(dto.folderId());
        return word;
    }
}
