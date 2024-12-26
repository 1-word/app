package com.numo.api.dto.quiz;

import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.quiz.type.QuizSort;
import com.numo.domain.quiz.type.QuizType;
import com.numo.domain.user.User;

public record QuizInfoRequestDto(
        QuizType type,
        QuizSort sort,
        String memorization,
        Integer count
) {
    public QuizInfo toEntity(Long userId) {
        User user = User.builder().userId(userId).build();
        return QuizInfo.builder()
                .user(user)
                .type(type)
                .sort(sort)
                .memorization(memorization)
                .count(count)
                .build();
    }
}
