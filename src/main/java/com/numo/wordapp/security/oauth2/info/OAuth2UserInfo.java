package com.numo.wordapp.security.oauth2.info;

import com.numo.wordapp.entity.user.Authority;
import com.numo.wordapp.entity.user.Role;
import com.numo.wordapp.entity.user.User;
import lombok.Builder;

import java.util.Set;

@Builder
public record OAuth2UserInfo(
        String socialId,
        String clientName,
        String email,
        String nickname,
        String thumbnail
) {
    public User toEntity() {
        Authority authority = Authority.builder()
                .name(Role.ROLE_USER)
                .build();
        return User.builder()
                .email(email)
                .nickname(nickname)
                .authorities(Set.of(authority))
                .serviceType(clientName)
                .build();
    }
}
