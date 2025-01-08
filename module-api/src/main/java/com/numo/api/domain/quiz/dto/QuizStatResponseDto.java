package com.numo.api.domain.quiz.dto;

import com.numo.domain.base.Timestamped;
import com.numo.domain.quiz.QuizStat;
import lombok.Builder;

@Builder
public record QuizStatResponseDto(
        Long quizStatId,
        int totalCount,
        int correctCount,
        int wrongCount,
        String createTime,
        String updateTime
) {
    public static QuizStatResponseDto of(QuizStat quizStat) {
        return QuizStatResponseDto.builder()
                .quizStatId(quizStat.getId())
                .totalCount(quizStat.getTotalCount())
                .correctCount(quizStat.getCorrectCount())
                .wrongCount(quizStat.getWrongCount())
                .createTime(Timestamped.getFormatTime(quizStat.getCreateTime(), "yyyy-MM-dd hh:mm:ss"))
                .updateTime(Timestamped.getFormatTime(quizStat.getUpdateTime(), "yyyy-MM-dd hh:mm:ss"))
                .build();
    }
}
