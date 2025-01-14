package com.numo.api.domain.quiz.repository;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.quiz.QuizInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface QuizInfoRepository extends JpaRepository<QuizInfo, Long> {
    @EntityGraph(attributePaths = {"folder", "user"})
    Optional<QuizInfo> findByIdAndUser_UserId(@Param("id") Long id, @Param("userId") Long userId);

    default QuizInfo findQuizInfo(Long id, Long userId) {
        return findByIdAndUser_UserId(id, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    @Modifying
    @Transactional
    @Query("delete from Quiz q " +
            "where q.quizInfo.id = :quizInfoId")
    void deleteAllQuiz(@Param("quizInfoId") Long quizInfoId);
}
