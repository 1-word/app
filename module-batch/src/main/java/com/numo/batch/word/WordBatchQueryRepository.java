package com.numo.batch.word;

import com.numo.domain.wordbook.word.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WordBatchQueryRepository {
    Slice<Word> findWordsBy(Long wordBookId, Pageable pageable);
}
