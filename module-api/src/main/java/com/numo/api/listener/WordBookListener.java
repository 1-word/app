package com.numo.api.listener;

import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.api.domain.wordbook.word.dto.WordCountDto;
import com.numo.api.domain.wordbook.word.service.WordService;
import com.numo.api.listener.event.WordCopyEvent;
import com.numo.api.listener.event.WordCountEvent;
import com.numo.batch.listener.WordBatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WordBookListener {
    private final WordBookService wordBookService;
    private final WordService wordService;
    private final ApplicationEventPublisher publisher;

    @EventListener
    @Async("asyncExecutor1")
    public void copyWord(WordCopyEvent event) {
        WordBatchEvent wordBatchEvent = new WordBatchEvent(event.userId(), event.wordBookId(), event.targetWordBookId());
        publisher.publishEvent(wordBatchEvent);
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

    private void asyncWordCount(Long wordBookId) {
        WordCountDto wordCount = wordService.getWordCountByWordBookId(wordBookId);
        wordBookService.asyncWordCount(wordBookId, wordCount);
    }
}
