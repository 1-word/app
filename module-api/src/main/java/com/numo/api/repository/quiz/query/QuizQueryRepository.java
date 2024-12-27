package com.numo.api.repository.quiz.query;

import com.numo.api.comm.page.PageUtil;
import com.numo.api.dto.quiz.QuizResponseDto;
import com.numo.domain.quiz.QQuiz;
import com.querydsl.core.types.Projections;
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

    public Slice<QuizResponseDto> findQuizById(Long quizInfoId, Long userId, Pageable pageable) {
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
                        qQuiz.quizInfo.user.userId.eq(userId)
                )
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .fetch();

        return PageUtil.of(results, pageable);
    }
}
