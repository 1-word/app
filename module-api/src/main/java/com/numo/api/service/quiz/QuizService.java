package com.numo.api.service.quiz;

import com.numo.api.comm.exception.CustomException;
import com.numo.api.comm.exception.ErrorCode;
import com.numo.api.comm.page.PageResponse;
import com.numo.api.dto.page.PageRequestDto;
import com.numo.api.dto.quiz.QuizInfoResponseDto;
import com.numo.api.dto.quiz.QuizResponseDto;
import com.numo.api.dto.quiz.QuizSolvedRequestDto;
import com.numo.api.repository.quiz.QuizRepository;
import com.numo.api.repository.quiz.query.QuizQueryRepository;
import com.numo.domain.quiz.Quiz;
import com.numo.domain.quiz.type.QuizSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizInfoService quizInfoService;
    private final QuizRepository quizRepository;
    private final QuizQueryRepository quizQueryRepository;
    private int pageSize = 30;

    public void createQuiz(Long userId, Long quizInfoId) {
        if (quizRepository.existsByQuizInfo_Id(quizInfoId)) {
            throw new CustomException(ErrorCode.QUIZ_DATA_EXISTS);
        }

        QuizInfoResponseDto quizInfo = quizInfoService.getQuizInfo(userId, quizInfoId);
        Long folderId = quizInfo.folderId();
        int limit = quizInfo.count();

        // 퀴즈 생성
        createQuiz(quizInfo.sort(), quizInfoId, folderId, userId, limit);
    }

    private void createQuiz(QuizSort sort, Long quizInfoId, Long folderId, Long userId, int limit) {
        switch (sort) {
            case created -> quizRepository.createQuizOrderByCreated(quizInfoId, folderId, userId, limit);
            case updated -> quizRepository.createQuizOrderByUpdated(quizInfoId, folderId, userId, limit);
            case random -> quizRepository.createQuizOrderByRandom(quizInfoId, folderId, userId, limit);
        }
    }

    public PageResponse<QuizResponseDto> getQuizInfo(Long userId, Long quizInfoId, PageRequestDto pageDto) {
        PageRequest pageRequest = PageRequest.of(pageDto.current(), pageSize);
        Slice<QuizResponseDto> quiz = quizQueryRepository.findQuizById(quizInfoId, userId, pageRequest);

        return new PageResponse<>(quiz);
    }

    @Transactional
    public void solveQuiz(Long userId, Long quizId, QuizSolvedRequestDto requestDto) {
        Quiz quiz = quizRepository.findQuizBy(quizId);
        quiz.setCorrect(requestDto.correct());
    }

    @Transactional
    public void solveQuizzes(Long userId, List<QuizSolvedRequestDto> requestDto) {
        List<Long> quizIds = requestDto.stream().map(QuizSolvedRequestDto::quizId).toList();
        List<Quiz> quizzes = quizRepository.findAllByIdIn(quizIds);

        Map<Long, Boolean> requestMap = requestDto.stream().collect(Collectors.toMap(QuizSolvedRequestDto::quizId, QuizSolvedRequestDto::correct));

        for (Quiz quiz : quizzes) {
            Boolean correct = requestMap.get(quiz.getId());
            quiz.setCorrect(correct);
        }
    }
}
