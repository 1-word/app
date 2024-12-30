package com.numo.api.domain.wordbook.word.repository.query;

import com.numo.api.domain.quiz.dto.QuizQuestionDto;
import com.numo.domain.word.QWord;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QWord qWord = QWord.word1;

    /**
     * 퀴즈 문제를 만들기 위해 단어 데이터를 조회한다.
     * @param folderId 폴더 아이디
     * @param userId 유저 아이디
     * @return 퀴즈 문제 데이터
     */
    public List<QuizQuestionDto> findQuizQuestion(Long folderId, Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        QuizQuestionDto.class,
                        qWord.wordId,
                        qWord.word,
                        qWord.mean
                )).from(qWord)
                .where(
                        qWord.folder.folderId.eq(folderId),
                        qWord.user.userId.eq(userId)
                )
                .fetch();
    }
}
