package com.numo.api.domain.quiz.dto.quizInfo;

import com.numo.domain.base.Timestamped;
import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.quiz.type.QuizType;
import com.numo.domain.wordbook.type.SortType;
import lombok.Builder;

@Builder
public record QuizInfoResponseDto(
        Long quizInfoId,
        Long wordBookId,
        QuizType type,
        SortType sort,
        String memorization,
        int count,
        String createTime,
        String updateTime
) {
    public static QuizInfoResponseDto of(QuizInfo quizInfo) {
        return of(quizInfo, quizInfo.getCount());
    }

    public static QuizInfoResponseDto of(QuizInfo quizInfo, int count) {
        return QuizInfoResponseDto.builder()
                .quizInfoId(quizInfo.getId())
                .wordBookId(quizInfo.getWordBook().getId())
                .type(quizInfo.getType())
                .sort(quizInfo.getSort())
                .memorization(quizInfo.getMemorization())
                .count(count)
                .createTime(Timestamped.getTimeString(quizInfo.getCreateTime()))
                .updateTime(Timestamped.getTimeString(quizInfo.getUpdateTime()))
                .build();
    }
}
