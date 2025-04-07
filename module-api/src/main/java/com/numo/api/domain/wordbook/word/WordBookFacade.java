package com.numo.api.domain.wordbook.word;

import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.api.domain.wordbook.word.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WordBookFacade {
    private final WordService wordService;
    private final WordBookService wordBookService;

    @Transactional
    public void removeWordBook(Long userId, Long wordBookId, boolean removeWords) {
        if (removeWords) {
            wordService.removeWordByWordBook(wordBookId);
        }
        wordBookService.removeWordBook(wordBookId, removeWords);
    }
}
