package com.numo.api.repository.quiz;

import com.numo.api.domain.quiz.repository.QuizRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class QuizRepositoryTest {

    @Autowired
    QuizRepository quizRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    void createQuizOrderByRandom() {
        Long quizInfoId = 1L;
        Long wordBookId = 6L;
        Long userId = 2L;
        int limit = 20;

        quizRepository.createQuizOrderByRandom(quizInfoId, wordBookId, userId, limit);
    }

}