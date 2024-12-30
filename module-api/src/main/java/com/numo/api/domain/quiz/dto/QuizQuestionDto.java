package com.numo.api.domain.quiz.dto;

public record QuizQuestionDto(
        Long wordId,
        String word,
        String mean
) {
}
