package com.numo.api.domain.quiz.dto;

public record QuizSolvedRequestDto(
        Long quizId,
        boolean correct
) {
}
