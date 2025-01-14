package com.numo.api.domain.quiz.repository.query;

import com.numo.api.domain.quiz.dto.QuizResponseDto;
import com.numo.api.global.comm.page.PageUtil;
import com.numo.domain.quiz.QQuiz;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuizQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private QQuiz qQuiz = QQuiz.quiz;

    public Slice<QuizResponseDto> findQuizById(Long quizInfoId, Long userId, Pageable pageable, boolean active) {
        List<QuizResponseDto> results = jpaQueryFactory.select(Projections.constructor(
                        QuizResponseDto.class,
                        qQuiz.id,
                        qQuiz.word.wordId,
                        qQuiz.word.word,
                        qQuiz.word.mean
                ))
                .from(qQuiz)
                .join(qQuiz.quizInfo)
                .join(qQuiz.word)
                .where(
                        qQuiz.quizInfo.id.eq(quizInfoId),
                        qQuiz.quizInfo.user.userId.eq(userId),
                        createUnsolvedQuizCondition(active)
                )
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .fetch();

        return PageUtil.of(results, pageable);
    }

    public Slice<QuizResponseDto> findUnsolvedQuizById(Long quizInfoId, Long userId, Pageable pageable) {
        return findQuizById(quizInfoId, userId, pageable, true);
    }

    private BooleanExpression createUnsolvedQuizCondition(boolean active) {
        if (!active) {
            return null;
        }
        return qQuiz.correct.isNull();
    }
}
