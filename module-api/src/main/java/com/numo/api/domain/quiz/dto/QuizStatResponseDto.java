package com.numo.api.domain.quiz.dto;

import com.numo.domain.Timestamped;
import com.numo.domain.quiz.QuizStat;
import lombok.Builder;

@Builder
public record QuizStatResponseDto(
        Long quizStatId,
        int totalCount,
        int correctCount,
        int wrongCount,
        int year,
        int month,
        int day,
        int week,
        String createTime,
        String updateTime
) {
    public static QuizStatResponseDto of(QuizStat quizStat) {
        return QuizStatResponseDto.builder()
                .quizStatId(quizStat.getId())
                .totalCount(quizStat.getTotalCount())
                .correctCount(quizStat.getCorrectCount())
                .wrongCount(quizStat.getWrongCount())
                .year(quizStat.getYear())
                .month(quizStat.getMonth())
                .day(quizStat.getDay())
                .week(quizStat.getWeek())
                .createTime(Timestamped.getTimeString(quizStat.getCreateTime()))
                .updateTime(Timestamped.getTimeString(quizStat.getUpdateTime()))
                .build();
    }
}
