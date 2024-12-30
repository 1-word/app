package com.numo.api.dto.quiz;

public record QuizSolvedRequestDto(
        Long quizId,
        boolean correct
) {
}
