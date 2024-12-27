package com.numo.api.repository.quiz;

import com.numo.api.comm.exception.CustomException;
import com.numo.api.comm.exception.ErrorCode;
import com.numo.domain.quiz.QuizInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizInfoRepository extends JpaRepository<QuizInfo, Long> {
    @EntityGraph(attributePaths = {"folder", "user"})
    Optional<QuizInfo> findByIdAndUser_UserId(@Param("id") Long id, @Param("userId") Long userId);

    default QuizInfo findQuizInfo(Long id, Long userId) {
        return findByIdAndUser_UserId(id, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}
