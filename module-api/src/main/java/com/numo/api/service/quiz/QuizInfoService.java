package com.numo.api.service.quiz;

import com.numo.api.dto.quiz.QuizInfoRequestDto;
import com.numo.api.dto.quiz.QuizInfoResponseDto;
import com.numo.api.repository.quiz.QuizInfoRepository;
import com.numo.domain.quiz.QuizInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void deleteQuizInfo(Long userId, Long quizInfoId) {
        QuizInfo quizInfo = quizInfoRepository.findQuizInfo(quizInfoId, userId);
        quizInfoRepository.delete(quizInfo);
        // 퀴즈 삭제 시 하위 퀴즈 데이터까지 모두 삭제해야함
    }
}
