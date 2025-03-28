package com.numo.api.domain.quiz.service;

import com.numo.api.domain.quiz.dto.QuizQuestionDto;
import com.numo.api.domain.quiz.dto.QuizResponseDto;
import com.numo.api.domain.quiz.dto.QuizSolvedRequestDto;
import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoResponseDto;
import com.numo.api.domain.quiz.dto.stat.QuizStatWordDto;
import com.numo.api.domain.quiz.repository.QuizRepository;
import com.numo.api.domain.quiz.repository.query.QuizQueryRepository;
import com.numo.api.domain.wordbook.word.repository.query.WordQueryRepository;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.global.comm.page.PageResponse;
import com.numo.domain.quiz.Quiz;
import com.numo.domain.wordbook.type.SortType;
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
     *
     * @param userId     유저 아이디
     * @param quizInfoId 퀴즈 정보 아이디
     * @return
     */
    public List<QuizQuestionDto> createQuiz(Long userId, Long quizInfoId) {
        if (quizRepository.existsByQuizInfo_Id(quizInfoId)) {
            throw new CustomException(ErrorCode.QUIZ_DATA_EXISTS);
        }

        QuizInfoResponseDto quizInfo = quizInfoService.getQuizInfo(userId, quizInfoId);
        Long wordBookId = quizInfo.wordBookId();
        int limit = quizInfo.count();

        // 퀴즈 생성
        createQuiz(quizInfo.sort(), quizInfoId, wordBookId, userId, limit);
        return getQuizQuestion(userId, wordBookId);
    }

    /**
     * 퀴즈 문제를 만들기 위해 단어 데이터 조회
     * @param userId 유저 아이디
     * @param wordBookId 폴더 아이디
     * @return 퀴즈 문제
     */
    public List<QuizQuestionDto> getQuizQuestion(Long userId, Long wordBookId) {
        return wordQueryRepository.findQuizQuestion(wordBookId, userId);
    }

    /**
     * 퀴즈 생성 조건에 따라 퀴즈를 생성한다.
     * @param sort 퀴즈 생성 조건
     * @param quizInfoId 퀴즈 정보 아이디
     * @param wordBookId 폴더 아이디
     * @param userId 유저 아이디
     * @param limit 퀴즈 개수
     */
    private void createQuiz(SortType sort, Long quizInfoId, Long wordBookId, Long userId, int limit) {
        switch (sort) {
            case created -> quizRepository.createQuizOrderByCreated(quizInfoId, wordBookId, userId, limit);
            case updated -> quizRepository.createQuizOrderByUpdated(quizInfoId, wordBookId, userId, limit);
            case random -> quizRepository.createQuizOrderByRandom(quizInfoId, wordBookId, userId, limit);
        }
    }

    /**
     * 퀴즈를 가져온다.
     *
     * @param userId     유저 아이디
     * @param quizInfoId 퀴즈 정보 아이디
     * @param pageDto    페이징 데이터
     * @param isContinue 이어하기 여부
     * @return 해당하는 퀴즈의 퀴즈 데이터
     */
    public PageResponse<QuizResponseDto> getQuizInfo(Long userId, Long quizInfoId, PageRequestDto pageDto, boolean isContinue) {
        PageRequest pageRequest = PageRequest.of(pageDto.current(), pageSize);
        Slice<QuizResponseDto> quiz = quizQueryRepository.findQuizById(quizInfoId, userId, pageRequest, isContinue);

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
        if (!quiz.isOwner(userId)) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }
        quiz.setCorrect(requestDto.correct());
    }

    /**
     * 다건으로 퀴즈를 풀이한다.
     * @param userId 유저 아이디
     * @param requestDto 퀴즈 맞춤 여부
     */
    @Transactional(rollbackFor = CustomException.class)
    public void solveQuizzes(Long userId, List<QuizSolvedRequestDto> requestDto) {
        List<Long> quizIds = requestDto.stream().map(QuizSolvedRequestDto::quizId).toList();
        List<Quiz> quizzes = quizRepository.findAllByIdIn(quizIds);

        Map<Long, Boolean> requestMap = requestDto.stream().collect(Collectors.toMap(QuizSolvedRequestDto::quizId, QuizSolvedRequestDto::correct));

        for (Quiz quiz : quizzes) {
            if (!quiz.isOwner(userId)) {
                throw new CustomException(ErrorCode.NOT_OWNER);
            }
            Boolean correct = requestMap.get(quiz.getId());
            quiz.setCorrect(correct);
        }
    }

    /**
     * 퀴즈 결과의 퀴즈 데이터를 조회한다
     *
     * @param userId     유저 아이디
     * @param quizInfoId 퀴즈 정보 아이디
     * @param pageDto    페이징 데이터
     * @param correct 전체, 정답, 오답 필터링 필터링 조회를 위한 boolean
     * @return 퀴즈 결과의 퀴즈 데이터
     */
    public PageResponse<QuizStatWordDto> getQuizResultWord(Long userId, Long quizInfoId, PageRequestDto pageDto, Boolean correct) {
        PageRequest pageRequest = PageRequest.of(pageDto.current(), pageSize);
        Slice<QuizStatWordDto> result = quizQueryRepository.findQuizResultWord(quizInfoId, userId, pageRequest, correct);

        return new PageResponse<>(result);
    }
}
