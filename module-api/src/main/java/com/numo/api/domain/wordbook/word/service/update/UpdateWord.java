package com.numo.api.domain.wordbook.word.service.update;

import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;

public interface UpdateWord {
    Word update(UpdateWordDto dto, Word word);
}
