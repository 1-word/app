package com.numo.api.domain.quiz.service;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.global.comm.page.PageResponse;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoResponseDto;
import com.numo.api.domain.quiz.dto.QuizQuestionDto;
import com.numo.api.domain.quiz.dto.QuizResponseDto;
import com.numo.api.domain.quiz.dto.QuizSolvedRequestDto;
import com.numo.api.domain.quiz.repository.QuizRepository;
import com.numo.api.domain.quiz.repository.query.QuizQueryRepository;
import com.numo.api.domain.wordbook.word.repository.query.WordQueryRepository;
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
    private final WordQueryRepository wordQueryRepository;
    private int pageSize = 30;

    /**
     * 퀴즈 생성 조건에 따라 퀴즈를 생성한다.
     * @param userId 유저 아이디
     * @param quizInfoId 퀴즈 정보 아이디
     */
    public List<QuizQuestionDto> createQuiz(Long userId, Long quizInfoId) {
        if (quizRepository.existsByQuizInfo_Id(quizInfoId)) {
            throw new CustomException(ErrorCode.QUIZ_DATA_EXISTS);
        }

        QuizInfoResponseDto quizInfo = quizInfoService.getQuizInfo(userId, quizInfoId);
        Long folderId = quizInfo.folderId();
        int limit = quizInfo.count();

        // 퀴즈 생성
        createQuiz(quizInfo.sort(), quizInfoId, folderId, userId, limit);

        // 퀴즈 관련 데이터 조회
        return getQuizQuestion(folderId, userId);
    }

    /**
     * 퀴즈 문제를 만들기 위해 단어 데이터 조회
     * @param folderId 폴더 아이디
     * @param userId 유저 아이디
     * @return 퀴즈 문제
     */
    private List<QuizQuestionDto> getQuizQuestion(Long folderId, Long userId) {
        return wordQueryRepository.findQuizQuestion(folderId, userId);
    }

    /**
     * 퀴즈 생성 조건에 따라 퀴즈를 생성한다.
     * @param sort 퀴즈 생성 조건
     * @param quizInfoId 퀴즈 정보 아이디
     * @param folderId 폴더 아이디
     * @param userId 유저 아이디
     * @param limit 퀴즈 개수
     */
    private void createQuiz(QuizSort sort, Long quizInfoId, Long folderId, Long userId, int limit) {
        switch (sort) {
            case created -> quizRepository.createQuizOrderByCreated(quizInfoId, folderId, userId, limit);
            case updated -> quizRepository.createQuizOrderByUpdated(quizInfoId, folderId, userId, limit);
            case random -> quizRepository.createQuizOrderByRandom(quizInfoId, folderId, userId, limit);
        }
    }

    /**
     * 퀴즈를 가져온다.
     * @param userId 유저 아이디
     * @param quizInfoId 퀴즈 정보 아이디
     * @param pageDto 페이징 데이터
     * @return 해당하는 퀴즈의 퀴즈 데이터
     */
    public PageResponse<QuizResponseDto> getQuizInfo(Long userId, Long quizInfoId, PageRequestDto pageDto) {
        PageRequest pageRequest = PageRequest.of(pageDto.current(), pageSize);
        Slice<QuizResponseDto> quiz = quizQueryRepository.findQuizById(quizInfoId, userId, pageRequest, false);

        return new PageResponse<>(quiz);
    }

    /**
     * 단건으로 퀴즈를 풀이한다.
     * @param userId 유저 아이디
     * @param quizId 퀴즈 아이디
     * @param requestDto 퀴즈 맞춤 여부
     */
    @Transactional
    public void solveQuiz(Long userId, Long quizId, QuizSolvedRequestDto requestDto) {
        Quiz quiz = quizRepository.findQuizBy(quizId);
        quiz.setCorrect(requestDto.correct());
    }

    /**
     * 다건으로 퀴즈를 풀이한다.
     * @param userId 유저 아이디
     * @param requestDto 퀴즈 맞춤 여부
     */
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

    /**
     * 퀴즈 이어 풀기를 위해 안 푼 퀴즈를 가져온다
     * @param userId 유저 아이디
     * @param quizInfoId 퀴즈 정보 아이디
     * @param pageDto 페이징 데이터
     * @return 안 푼 퀴즈 데이터
     */
    public PageResponse<QuizResponseDto> getUnsolvedQuiz(Long userId, Long quizInfoId, PageRequestDto pageDto) {
        PageRequest pageRequest = PageRequest.of(pageDto.current(), pageSize);
        Slice<QuizResponseDto> quiz = quizQueryRepository.findUnsolvedQuizById(quizInfoId, userId, pageRequest);

        return new PageResponse<>(quiz);
    }
}