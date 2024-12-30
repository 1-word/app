package com.numo.api.domain.quiz.service;

import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoRequestDto;
import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoResponseDto;
import com.numo.api.domain.quiz.repository.QuizInfoRepository;
import com.numo.domain.quiz.QuizInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizInfoService {
    private final QuizInfoRepository quizInfoRepository;

    public Long createQuizInfo(Long userId, QuizInfoRequestDto requestDto) {
        QuizInfo quizInfo = requestDto.toEntity(userId);
        return quizInfoRepository.save(quizInfo).getId();
    }

    public QuizInfoResponseDto getQuizInfo(Long userId, Long quizInfoId) {
        QuizInfo quizInfo = quizInfoRepository.findQuizInfo(quizInfoId, userId);
        return QuizInfoResponseDto.of(quizInfo);
    }

    @Transactional
    public void deleteQuizInfo(Long userId, Long quizInfoId) {
        QuizInfo quizInfo = quizInfoRepository.findQuizInfo(quizInfoId, userId);
        quizInfoRepository.delete(quizInfo);

        // 연관된 퀴즈 데이터 모두 삭제
        quizInfoRepository.deleteAllQuiz(quizInfoId);
    }

    @Transactional
    public void completeQuiz(Long userId, Long quizInfoId) {
        QuizInfo quizInfo = quizInfoRepository.findQuizInfo(quizInfoId, userId);
        quizInfo.quizComplete();
    }
}
