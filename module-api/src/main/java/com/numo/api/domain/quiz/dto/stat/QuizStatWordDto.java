package com.numo.api.domain.quiz.dto.stat;

public record QuizStatWordDto(
        Long quizId,
        Long wordId,
        String word,
        String read,
        String mean,
        boolean correct
) {
    public QuizStatWordDto(Long quizId, Long wordId, String word, String read, String mean, Integer correct) {
        this(quizId, wordId, word, read, mean, correct.equals(1));
    }
}
