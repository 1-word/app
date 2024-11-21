package com.numo.wordapp.dto.word.group;

import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.entity.word.detail.WordGroup;
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
