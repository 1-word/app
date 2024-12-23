package com.numo.api.service.word.update;

import com.numo.domain.word.Word;
import com.numo.domain.word.dto.UpdateWordDto;

public class UpdateFolder implements UpdateWord {
    @Override
    public Word update(UpdateWordDto dto, Word word) {
        word.setFolder(dto.folderId());
        return word;
    }
}
