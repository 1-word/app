package com.numo.api.domain.quiz.service;

import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoRequestDto;
import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoResponseDto;
import com.numo.api.domain.quiz.repository.QuizInfoRepository;
import com.numo.api.domain.quiz.repository.QuizRepository;
import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizInfoService {
    private final WordBookService wordBookService;
    private final QuizInfoRepository quizInfoRepository;
    private final QuizRepository quizRepository;

    public Long createQuizInfo(Long userId, QuizInfoRequestDto requestDto) {
        User user = new User(userId);
        WordBook wordBook = wordBookService.findWordBook(requestDto.wordBookId());
        QuizInfo quizInfo = requestDto.toEntity(user, wordBook);
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

    public boolean isCompleteQuiz(Long userId, Long quizInfoId) {
        QuizInfo quizInfo = quizInfoRepository.findQuizInfo(quizInfoId, userId);
        return quizInfo.isComplete();
    }

    public QuizInfoResponseDto getInCompleteQuizInfo(Long userId) {
        QuizInfo quizInfo = quizInfoRepository.findTopByUser_UserIdAndCompleteOrderByIdDesc(userId, false);
        if (quizInfo == null) {
            return QuizInfoResponseDto.builder().build();
        }
        Long count = quizRepository.countByQuizInfo_IdAndCorrectIsNull(quizInfo.getId());
        return QuizInfoResponseDto.of(quizInfo, count.intValue());
    }
}
