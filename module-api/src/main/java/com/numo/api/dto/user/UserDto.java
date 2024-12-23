package com.numo.api.dto.user;

import com.numo.domain.user.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserDto(
        Long userId,
        String email,
        String nickname,
        String profileImagePath,
        LocalDateTime withdrawDate,
        List<String> authorities
) {
    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImagePath(user.getProfileImagePath())
                .withdrawDate(user.getWithdrawDate())
                .authorities(user.getAuthNameList())
                .build();
    }
}
