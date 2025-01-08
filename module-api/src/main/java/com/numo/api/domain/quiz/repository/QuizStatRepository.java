package com.numo.api.domain.quiz.repository;

import com.numo.api.domain.quiz.dto.QuizResultDto;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.quiz.QuizStat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuizStatRepository extends JpaRepository<QuizStat, Long> {

    boolean existsByQuizInfo_Id(Long quizInfoId);

    @Query(value = "select " +
            "    count(*) as totalCount, " +
            "    count(case when correct = 1 then 1 end) as correctCount, " +
            "    count(case when correct = 0 then 1 end) as wrongCount " +
            "from quiz " +
            "where quiz_info_id = 15", nativeQuery = true)
    QuizResultDto findQuiz(Long quizInfoId, Long userId);

    @EntityGraph(attributePaths = {"quizInfo"})
    Optional<QuizStat> findByIdAndUser_UserId(Long quizStatId, Long userId);

    default QuizStat findQuizStatByIdAndUserId(Long quizStatId, Long userId) {
        return findByIdAndUser_UserId(quizStatId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}
