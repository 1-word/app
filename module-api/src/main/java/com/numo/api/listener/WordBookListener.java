package com.numo.api.listener;

import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.api.domain.wordbook.word.dto.WordCountDto;
import com.numo.api.domain.wordbook.word.service.WordService;
import com.numo.api.listener.event.WordBookEvent;
import com.numo.api.listener.event.WordCountEvent;
import com.numo.batch.listener.WordBatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WordBookListener {
    private final WordBookService wordBookService;
    private final WordService wordService;

    /**
     * 옮긴 단어장의 count 동기화
     * @param event 단어장 정보
     */
    @EventListener
    public void moveWordBook(WordBookEvent event) {
        wordBookService.incrementWordCount(event.id(), event.memorization());
    }

    /**
     * 해당 단어장의 count 동기화 (배치 이벤트)
     */
    @EventListener
    @Order(2)
    public void asyncWordCount(WordBatchEvent event) {
        asyncWordCount(event.targetWordBookId());
    }

    /**
     * 해당 단어장의 count 동기화 (일반)
     */
    @TransactionalEventListener
    @Async("asyncExecutor1")
    public void asyncWordCount(WordCountEvent event) {
        asyncWordCount(event.wordBookId());
    }

    public void asyncWordCount(Long wordBookId) {
        WordCountDto wordCount = wordService.getWordCountByWordBookId(wordBookId);
        wordBookService.asyncWordCount(wordBookId, wordCount);
    }
}
