package com.numo.api.repository.quiz;

import com.numo.api.comm.exception.CustomException;
import com.numo.api.comm.exception.ErrorCode;
import com.numo.domain.quiz.QuizInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizInfoRepository extends JpaRepository<QuizInfo, Long> {
    Optional<QuizInfo> findByIdAndUser_UserId(Long id, Long userId);

    default QuizInfo findQuizInfo(Long id, Long userId) {
        return findByIdAndUser_UserId(id, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}
