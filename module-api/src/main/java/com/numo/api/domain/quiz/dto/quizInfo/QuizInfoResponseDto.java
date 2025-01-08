package com.numo.api.domain.quiz.dto.quizInfo;

import com.numo.domain.base.Timestamped;
import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.quiz.type.QuizSort;
import com.numo.domain.quiz.type.QuizType;
import lombok.Builder;

@Builder
public record QuizInfoResponseDto(
        Long quizInfoId,
        Long folderId,
        QuizType type,
        QuizSort sort,
        String memorization,
        int count,
        String createTime,
        String updateTime
) {
    public static QuizInfoResponseDto of(QuizInfo quizInfo) {
        return QuizInfoResponseDto.builder()
                .quizInfoId(quizInfo.getId())
                .folderId(quizInfo.getFolder().getFolderId())
                .type(quizInfo.getType())
                .sort(quizInfo.getSort())
                .memorization(quizInfo.getMemorization())
                .count(quizInfo.getCount())
                .createTime(Timestamped.getTimeString(quizInfo.getCreateTime()))
                .updateTime(Timestamped.getTimeString(quizInfo.getUpdateTime()))
                .build();
    }
}
