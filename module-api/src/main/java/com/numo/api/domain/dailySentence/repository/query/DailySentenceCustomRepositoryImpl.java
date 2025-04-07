package com.numo.api.domain.dailySentence.repository.query;

import com.numo.api.domain.dailySentence.dto.DailySentenceDto;
import com.numo.api.domain.dailySentence.dto.read.ReadDailyWordDto;
import com.numo.api.domain.dailySentence.dto.wordDailySentence.WordDailySentenceDto;
import com.numo.api.global.comm.date.DateCondition;
import com.numo.api.global.comm.date.DateRequestDto;
import com.numo.domain.sentence.QDailySentence;
import com.numo.domain.sentence.QWordDailySentence;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DailySentenceCustomRepositoryImpl implements DailySentenceCustomRepository {
    private final JPAQueryFactory queryFactory;
    QDailySentence qDailySentence = QDailySentence.dailySentence;

    /**
     * 연도, 월, 일, 주별 문장 데이터 조회
     * parameterDto에 값이 없으면 해당하는 모든 유저 문장 데이터를 조회
     *
     * @param userId       유저 아이디
     * @param parameterDto 연도, 월, 일, 주별 데이터
     * @return 조회된 연도, 월, 일, 주별 문장 데이터 조회
     */
    @Override
    public List<DailySentenceDto> findDailySentencesBy(Long userId, DateRequestDto parameterDto) {
        List<DailySentenceDto> result = queryFactory.select(
                        Projections.constructor(
                                DailySentenceDto.class,
                                qDailySentence.dailySentenceId,
                                qDailySentence.sentence,
                                qDailySentence.tagSentence,
                                qDailySentence.mean,
                                qDailySentence.baseDate.year,
                                qDailySentence.baseDate.month,
                                qDailySentence.baseDate.week,
                                qDailySentence.baseDate.day,
                                qDailySentence.createTime,
                                qDailySentence.updateTime
                        ))
                .from(qDailySentence)
                .where(
                        qDailySentence.user.userId.eq(userId),
                        DateCondition.createDateCondition(qDailySentence.baseDate, parameterDto)
                )
                .fetch();
        return result;
    }

    /**
     * 문장 고유번호 리스트에 연관된 단어 조회
     *
     * @param sentenceIds 문장 고유번호 리스트
     * @return 문장 고유번호 리스트에 연관된 단어 데이터
     */
    public List<ReadDailyWordDto> findDailyWordsBy(List<Long> sentenceIds) {
        QWordDailySentence qWordDailySentence = QWordDailySentence.wordDailySentence;
        List<ReadDailyWordDto> result = queryFactory.select(Projections.constructor(
                        ReadDailyWordDto.class,
                        qWordDailySentence.dailySentence.dailySentenceId,
                        qWordDailySentence.word.wordId,
                        qWordDailySentence.word.word,
                        qWordDailySentence.word.mean,
                        qWordDailySentence.word.wordBook.id,
                        qWordDailySentence.word.wordBook.name
                ))
                .from(qWordDailySentence)
                .leftJoin(qWordDailySentence.word)
                .where(
                        qWordDailySentence.dailySentence.dailySentenceId.in(sentenceIds)
                )
                .fetch();
        return result;
    }

    @Override
    public List<Integer> findDailySentenceDays(Long userId, DateRequestDto parameterDto) {
        return queryFactory.selectDistinct(Projections.constructor(
                        Integer.class,
                        qDailySentence.baseDate.day
                )).from(qDailySentence)
                .where(
                        qDailySentence.user.userId.eq(userId),
                        qDailySentence.baseDate.year.eq(parameterDto.year()),
                        qDailySentence.baseDate.month.eq(parameterDto.month())
                ).fetch();
    }

    @Override
    public List<WordDailySentenceDto> getWordDailySentenceInfo(Long userId, Long wordDailySentenceId) {
        QWordDailySentence qWordDailySentence = QWordDailySentence.wordDailySentence;
        return queryFactory.selectDistinct(Projections.constructor(
                        WordDailySentenceDto.class,
                        qWordDailySentence.word.wordId,
                        qWordDailySentence.word.wordBook.id,
                        qWordDailySentence.matchedWord
                )).from(qWordDailySentence)
                .where(
                        qWordDailySentence.dailySentence.user.userId.eq(userId),
                        qWordDailySentence.dailySentence.dailySentenceId.eq(wordDailySentenceId)
                )
                .fetch();
    }
}
