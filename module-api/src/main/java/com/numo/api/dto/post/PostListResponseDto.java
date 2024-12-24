package com.numo.api.dto.post;

import com.numo.domain.Timestamped;

import java.time.LocalDateTime;

public record PostListResponseDto(
        Long postId,
        String nickname,
        String title,
        String createTime,
        String updateTime
) {
    public PostListResponseDto(Long postId, String nickname, String title, LocalDateTime createTime, LocalDateTime updateTime) {
       this(postId, nickname, title, Timestamped.getTimeString(createTime), Timestamped.getTimeString(updateTime));
    }
}
