package com.numo.api.domain.quiz.service;

import com.numo.api.domain.quiz.dto.QuizResultDto;
import com.numo.api.domain.quiz.dto.QuizStatResponseDto;
import com.numo.api.domain.quiz.repository.QuizStatRepository;
import com.numo.api.domain.quiz.repository.query.QuizStatQueryRepository;
import com.numo.api.global.comm.date.DateRequestDto;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.quiz.QuizStat;
import com.numo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizStatService {
    private final QuizStatRepository quizStatRepository;
    private final QuizInfoService quizInfoService;
    private final QuizStatQueryRepository quizStatQueryRepository;

    /**
     * 통계 데이터를 생성한다
     * @param quizInfoId 퀴즈 정보 아이디
     * @param userId 유저 아이디
     * @return 생성한 퀴즈 통계 데이터
     */
    public QuizStatResponseDto createQuizStat(Long quizInfoId, Long userId) {
        if (!quizInfoService.isCompleteQuiz(userId, quizInfoId)) {
            throw new CustomException(ErrorCode.QUIZ_NOT_FINISHED);
        }

        if (quizStatRepository.existsByQuizInfo_Id(quizInfoId)) {
            throw new CustomException(ErrorCode.QUIZ_STAT_EXISTS);
        }
        
        QuizResultDto quizResult = quizStatRepository.findQuiz(quizInfoId, userId);

        QuizStat quizStat = QuizStat.builder()
                .quizInfo(new QuizInfo(quizInfoId))
                .user(User.builder().userId(userId).build())
                .totalCount(quizResult.getTotalCount())
                .correctCount(quizResult.getCorrectCount())
                .wrongCount(quizResult.getWrongCount())
                .build();

        return QuizStatResponseDto.of(quizStatRepository.save(quizStat));
    }

    /**
     * 퀴즈 통계 데이터를 가져온다
     * @param quizStatId 퀴즈 통계 아이디
     * @param userId 유저 아이디
     * @return 퀴즈 통계 데이터
     */
    public QuizStatResponseDto getQuizStat(Long quizStatId, Long userId) {
        QuizStat quizStat = quizStatRepository.findQuizStatByIdAndUserId(quizStatId, userId);
        return QuizStatResponseDto.of(quizStat);
    }

    /**
     * 퀴즈 통계 리스트를 가져온다.
     * @param userId 유저 아이디
     * @param dateRequest 검색 날짜
     * @return 퀴즈 통계 리스트
     */
    public List<QuizStatResponseDto> getQuizStatList(Long userId, DateRequestDto dateRequest) {
        return quizStatQueryRepository.findQuizStatList(userId, dateRequest);
    }

    /**
     * 퀴즈 통계 데이터를 삭제한다.
     * @param userId 유저 아이디
     * @param quizStatId 퀴즈 통계 아이디
     */
    public void deleteQuizStat(Long userId, Long quizStatId) {
        QuizStat quizStat = quizStatRepository.findQuizStatByIdAndUserId(quizStatId, userId);
        quizStatRepository.delete(quizStat);
    }
}
