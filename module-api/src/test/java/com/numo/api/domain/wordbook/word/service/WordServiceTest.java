package com.numo.api.domain.wordbook.word.service;

import com.numo.batch.listener.WordBatchEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class WordServiceTest {
    @Autowired
    WordService wordService;

    @Autowired
    ApplicationEventPublisher publisher;

    Long userId = 36L;
    Long wordBookId = 43L;
    Long targetWordBookId = 44L;

    @Test
    void copyWord() {
        WordBatchEvent event = new WordBatchEvent(userId, wordBookId, targetWordBookId);
        publisher.publishEvent(event);
    }


}