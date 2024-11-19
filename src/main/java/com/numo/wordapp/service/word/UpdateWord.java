package com.numo.wordapp.service.word;

import com.numo.wordapp.dto.word.UpdateWordDto;
import com.numo.wordapp.entity.word.Word;

public interface UpdateWord {
    Word update(UpdateWordDto dto, Word word);
}
