package com.numo.api.dto.quiz;

public record QuizQuestionDto(
        Long wordId,
        String word,
        String mean
) {
}
