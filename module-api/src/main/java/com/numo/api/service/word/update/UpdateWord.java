package com.numo.api.service.word.update;

import com.numo.domain.word.Word;
import com.numo.domain.word.dto.UpdateWordDto;

public interface UpdateWord {
    Word update(UpdateWordDto dto, Word word);
}
