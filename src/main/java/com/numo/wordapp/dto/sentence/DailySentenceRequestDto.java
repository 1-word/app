package com.numo.wordapp.dto.sentence;

import com.numo.wordapp.entity.sentence.DailySentence;
import com.numo.wordapp.entity.user.User;

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
