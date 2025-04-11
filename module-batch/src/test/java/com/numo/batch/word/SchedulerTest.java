package com.numo.batch.word;

import com.numo.batch.listener.WordBatchEvent;
import com.numo.batch.quiz.QuizBatchScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class SchedulerTest {
    @Autowired
    WordBatchScheduler wordBatchScheduler;

    @Autowired
    QuizBatchScheduler quizBatchScheduler;

    @Autowired
    ApplicationEventPublisher publisher;

    @Test
    void runJob() {
        Long userId = 36L;
        Long wordBookId = 43L;
        Long targetWordBookId = 44L;

        wordBatchScheduler.runJob(userId, wordBookId, targetWordBookId);
    }

    @Test
    void quizJobRun() {
        quizBatchScheduler.runJob();
    }

    @Test
    void eventPublish() {
        Long userId = 36L;
        Long wordBookId = 43L;
        Long targetWordBookId = 44L;
        WordBatchEvent event = new WordBatchEvent(userId, wordBookId, targetWordBookId);
        publisher.publishEvent(event);
    }
}