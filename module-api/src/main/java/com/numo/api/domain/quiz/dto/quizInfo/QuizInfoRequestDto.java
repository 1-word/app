package com.numo.api.domain.quiz.dto.quizInfo;

import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.quiz.type.QuizType;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.type.SortType;

public record QuizInfoRequestDto(
        @Deprecated
        Long folderId,
        Long wordBookId,
        QuizType type,
        SortType sort,
        String memorization,
        Integer count
) {
    public QuizInfoRequestDto {
        wordBookId = folderId;
    }

    public QuizInfo toEntity(User user, WordBook wordBook) {
        return QuizInfo.builder()
                .user(user)
                .wordBook(wordBook)
                .type(type)
                .sort(sort)
                .memorization(memorization)
                .count(count)
                .build();
    }
}
