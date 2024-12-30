package com.numo.api.domain.quiz.dto;

import lombok.Builder;

@Builder
public record QuizResponseDto(
        Long quizId,
        Long wordId,
        String word,
        String mean
) {
}
