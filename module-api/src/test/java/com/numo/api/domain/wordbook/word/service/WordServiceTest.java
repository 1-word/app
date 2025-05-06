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

    Long userId = 9L;
    Long wordBookId = 66L;
    Long targetWordBookId = 66L;

    @Test
    void copyWord() {
        WordBatchEvent event = new WordBatchEvent(userId, wordBookId, targetWordBookId);
        publisher.publishEvent(event);
    }


}