package com.numo.api.domain.quiz.dto.quizInfo;

import com.numo.domain.quiz.QuizInfo;
import com.numo.domain.quiz.type.QuizSort;
import com.numo.domain.quiz.type.QuizType;
import com.numo.domain.user.User;
import com.numo.domain.word.folder.Folder;

public record QuizInfoRequestDto(
        Long folderId,
        QuizType type,
        QuizSort sort,
        String memorization,
        Integer count
) {
    public QuizInfo toEntity(Long userId) {
        User user = User.builder().userId(userId).build();
        Folder folder = Folder.builder().folderId(folderId).build();
        return QuizInfo.builder()
                .user(user)
                .folder(folder)
                .type(type)
                .sort(sort)
                .memorization(memorization)
                .count(count)
                .build();
    }
}
