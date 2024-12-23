package com.numo.domain.sentence.dto;

import com.numo.domain.sentence.DailySentence;
import com.numo.domain.user.User;

public record DailySentenceRequestDto(
        String sentence,
        String mean
) {
    public DailySentence toEntity(Long userId) {
        User user = User.builder().userId(userId).build();
        return DailySentence.builder()
                .user(user)
                .sentence(sentence)
                .mean(mean)
                .build();
    }
}
