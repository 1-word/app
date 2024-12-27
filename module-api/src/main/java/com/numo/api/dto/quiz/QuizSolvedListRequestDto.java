package com.numo.api.dto.quiz;

import java.util.List;

public record QuizSolvedListRequestDto(
        List<QuizSolvedRequestDto> datas
) {
}
