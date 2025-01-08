package com.numo.api.domain.quiz.repository.query;

import com.numo.api.domain.quiz.dto.QuizResultDto;
import com.numo.api.domain.quiz.repository.QuizStatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizQueryRepositoryTest {
    @Autowired
    QuizStatRepository quizStatRepository;

    @Test
    void findQuiz() {
        Long quizInfoId = 15L;
        Long userId = 2L;

        QuizResultDto quiz = quizStatRepository.findQuiz(quizInfoId, userId);
        System.out.println(quiz);
    }
}