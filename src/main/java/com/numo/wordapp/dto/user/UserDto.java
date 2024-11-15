package com.numo.wordapp.dto.user;

import com.numo.wordapp.entity.user.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserDto(
        Long userId,
        String email,
        String nickname,
        String thumbnailId,
        LocalDateTime withdrawDate,
        List<String> authorities
) {
    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .thumbnailId(user.getThumbnailId())
                .withdrawDate(user.getWithdrawDate())
                .authorities(user.getAuthNameList())
                .build();
    }
}
