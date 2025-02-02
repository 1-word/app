package com.numo.api.domain.quiz.repository.query;

import com.numo.api.domain.quiz.dto.stat.QuizStatResponseDto;
import com.numo.api.global.comm.date.DateCondition;
import com.numo.api.global.comm.date.DateRequestDto;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.quiz.QQuizStat;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuizStatQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QQuizStat qQuizStat = QQuizStat.quizStat;

    public QuizStatResponseDto findQuiz(Long quizStatId, Long userId, DateRequestDto dateRequest) {
        List<QuizStatResponseDto> result = findQuizStatList(quizStatId, userId, dateRequest);
        if (result.isEmpty()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }
        return result.get(0);
    }

    public List<QuizStatResponseDto> findQuizStatList(Long quizStatId, Long userId, DateRequestDto dateRequest) {
        return jpaQueryFactory.select(Projections.constructor(
                        QuizStatResponseDto.class,
                        qQuizStat.id,
                        qQuizStat.quizInfo.folder.folderName,
                        qQuizStat.totalCount,
                        qQuizStat.correctCount,
                        qQuizStat.wrongCount,
                        qQuizStat.createTime,
                        qQuizStat.updateTime
                )).from(qQuizStat)
                .where(
                        quizStatIdEq(quizStatId),
                        qQuizStat.user.userId.eq(userId),
                        DateCondition.createDateCondition(qQuizStat.baseDate, dateRequest)
                ).fetch();
    }

    private BooleanExpression quizStatIdEq(Long quizStatId) {
        if (quizStatId == null) {
            return null;
        }
        return qQuizStat.id.eq(quizStatId);
    }

}
