package com.numo.api.domain.quiz.dto;

import java.util.List;

public record QuizSolvedListRequestDto(
        List<QuizSolvedRequestDto> datas
) {
}
