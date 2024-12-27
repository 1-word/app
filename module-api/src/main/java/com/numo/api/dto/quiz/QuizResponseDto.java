package com.numo.api.dto.quiz;

import lombok.Builder;

@Builder
public record QuizResponseDto(
        Long quizId,
        Long wordId,
        String word,
        String mean
) {
}
