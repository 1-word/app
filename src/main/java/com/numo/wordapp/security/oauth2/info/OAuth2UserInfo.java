package com.numo.wordapp.security.oauth2.info;

import com.numo.wordapp.entity.user.User;
import lombok.Builder;

@Builder
public record OAuth2UserInfo(
        String socialId,
        String clientName,
        String email,
        String nickname,
        String thumbnail
) {
    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .profileImagePath(thumbnail)
                .serviceType(clientName)
                .build();
    }
}
