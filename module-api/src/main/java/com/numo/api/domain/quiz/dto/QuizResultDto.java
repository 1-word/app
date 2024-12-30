package com.numo.api.domain.quiz.dto;

public interface QuizResultDto {
    Integer getTotalCount();
    Integer getCorrectCount();
    Integer getWrongCount();
}
