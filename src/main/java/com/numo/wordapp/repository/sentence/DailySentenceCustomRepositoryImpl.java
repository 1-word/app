package com.numo.wordapp.repository.sentence;

import com.numo.wordapp.dto.sentence.DailySentenceDto;
import com.numo.wordapp.dto.sentence.DailySentenceParameterDto;
import com.numo.wordapp.dto.sentence.DailyWordDto;
import com.numo.wordapp.entity.sentence.QDailySentence;
import com.numo.wordapp.entity.word.QWordDailySentence;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
     * @param userId 유저 아이디
     * @param parameterDto 연도, 월, 일, 주별 데이터
     * @return 조회된 연도, 월, 일, 주별 문장 데이터 조회
     */
    @Override
    public List<DailySentenceDto> findDailySentencesBy(Long userId, DailySentenceParameterDto parameterDto) {
        List<DailySentenceDto> result = queryFactory.select(
                        Projections.constructor(
                                DailySentenceDto.class,
                                qDailySentence.dailySentenceId,
                                qDailySentence.sentence,
                                qDailySentence.mean,
                                qDailySentence.year,
                                qDailySentence.month,
                                qDailySentence.week,
                                qDailySentence.day,
                                qDailySentence.createTime,
                                qDailySentence.updateTime
                        ))
                .from(qDailySentence)
                .where(
                        qDailySentence.user.userId.eq(userId),
                        createDailySentenceCondition(parameterDto)
                )
                .fetch();
        return result;
    }

    /**
     * 파라미터에 해당하는 쿼리 조건문을 만든다
     * @param parameterDto 조건문에 들어갈 필드 값
     * @return 파라미터에 해당하는 쿼리 조건문
     */
    private BooleanExpression createDailySentenceCondition(DailySentenceParameterDto parameterDto) {
        BooleanExpression result = Expressions.asString("1").eq("1");
        Integer year = parameterDto.year();
        Integer month = parameterDto.month();
        Integer day = parameterDto.day();
        Integer week = parameterDto.week();

        if (year != null) {
            result = result.and(qDailySentence.year.eq(year));
        }

        if (month != null) {
            result = result.and(qDailySentence.month.eq(month));
        }

        if (day != null) {
            result = result.and(qDailySentence.day.eq(day));
        }

        if (week != null) {
            result = result.and(qDailySentence.week.eq(week));
        }

        return result;
    }

    /**
     * 문장 고유번호 리스트에 연관된 단어 조회
     * @param sentenceIds 문장 고유번호 리스트
     * @return 문장 고유번호 리스트에 연관된 단어 데이터
     */
    public List<DailyWordDto> findDailyWordsBy(List<Long> sentenceIds) {
        QWordDailySentence qWordDailySentence = QWordDailySentence.wordDailySentence;
        List<DailyWordDto> result = queryFactory.select(Projections.constructor(
                DailyWordDto.class,
                qWordDailySentence.dailySentence.dailySentenceId,
                qWordDailySentence.word.wordId,
                qWordDailySentence.word.word,
                qWordDailySentence.word.mean,
                qWordDailySentence.word.folder.folderId,
                qWordDailySentence.word.folder.folderName
                ))
                .from(qWordDailySentence)
                .leftJoin(qWordDailySentence.word)
                .where(
                        qWordDailySentence.dailySentence.dailySentenceId.in(sentenceIds)
                )
                .fetch();
        return result;
    }
}
