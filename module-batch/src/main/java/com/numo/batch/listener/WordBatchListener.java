package com.numo.batch.listener;

import com.numo.batch.word.WordBatchScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WordBatchListener {

    private final WordBatchScheduler wordBatchScheduler;

    @EventListener
    @Order(1)
    public void copyWord(WordBatchEvent event) {
        log.debug("단어 복사 시작, 대상 단어장: {} 복사 단어장: {}", event.targetWordBookId(), event.wordBookId());
        wordBatchScheduler.runJob(event.userId(), event.wordBookId(), event.targetWordBookId());
    }
}
