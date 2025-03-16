package com.numo.api.domain.wordbook.dto;

import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import lombok.Builder;

@Builder
public record WordBookRequestDto(
        String name,
        String memo,
        String color,
        String background
) {
    public WordBook toEntity(Long userId) {
        User user = User.builder().userId(userId).build();
        return WordBook.builder()
                .user(user)
                .name(name)
                .memo(memo)
                .color(color)
                .background(background)
                .build();
    }
}
