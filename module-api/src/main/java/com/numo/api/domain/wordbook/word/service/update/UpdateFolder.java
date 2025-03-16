package com.numo.api.domain.wordbook.word.service.update;

import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;

// 단어장 이동 api가 따로 있어 삭제
@Deprecated
public class UpdateFolder implements UpdateWord {
    @Override
    public Word update(UpdateWordDto dto, Word word) {
        return word;
    }
}
