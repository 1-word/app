package com.numo.api.domain.quiz.repository;

import com.numo.api.domain.quiz.dto.QuizResultDto;
import com.numo.domain.quiz.QuizStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizStatRepository extends JpaRepository<QuizStat, Long> {

    @Query(value = "select " +
            "    count(*) as totalCount, " +
            "    count(case when correct = 1 then 1 end) as correctCount, " +
            "    count(case when correct = 0 then 1 end) as wrongCount " +
            "from quiz " +
            "where quiz_info_id = 15", nativeQuery = true)
    QuizResultDto findQuiz(Long quizInfoId, Long userId);
}
