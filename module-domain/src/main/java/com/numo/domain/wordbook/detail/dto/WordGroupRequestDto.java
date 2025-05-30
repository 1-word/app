package com.numo.domain.wordbook.detail.dto;

import com.numo.domain.user.User;
import com.numo.domain.wordbook.detail.WordGroup;
import lombok.Builder;

@Builder
public record WordGroupRequestDto(
        String name,
        String description
) {
    public WordGroup toEntity(Long userId) {
        User user = User.builder().userId(userId).build();
        return WordGroup.builder()
                .user(user)
                .name(name)
                .description(description)
                .build();
    }
}
