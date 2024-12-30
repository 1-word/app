package com.numo.api.domain.quiz.service;

import com.numo.api.domain.quiz.dto.QuizResultDto;
import com.numo.api.domain.quiz.dto.QuizStatResponseDto;
import com.numo.api.domain.quiz.repository.QuizStatRepository;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.quiz.QuizStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizStatService {
    private final QuizInfoService QuizInfoService;
    private final QuizStatRepository quizStatRepository;
    private final QuizInfoService quizInfoService;

    /**
     * 퀴즈에 해당하는 정보를
     * @param quizInfoId
     * @param userId
     * @return
     */
    public QuizStatResponseDto createQuizStat(Long quizInfoId, Long userId) {
        if (!quizInfoService.isCompleteQuiz(userId, quizInfoId)) {
            throw new CustomException(ErrorCode.QUIZ_NOT_FINISHED);
        }
        
        QuizResultDto quizResult = quizStatRepository.findQuiz(quizInfoId, userId);

        QuizStat quizStat = QuizStat.builder()
                .quizInfo(new QuizInfo(quizInfoId))
                .totalCount(quizResult.getTotalCount())
                .correctCount(quizResult.getCorrectCount())
                .wrongCount(quizResult.getWrongCount())
                .build();

        return QuizStatResponseDto.of(quizStatRepository.save(quizStat));
    }
}
