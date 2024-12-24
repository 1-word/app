package com.numo.api.dto.post;

import com.numo.domain.Timestamped;
import com.numo.domain.post.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponseDto(
        Long postId,
        String nickname,
        String title,
        String content,
        String createTime,
        String updateTime
) {
    public PostResponseDto(Long postId, String nickname, String title, String content, LocalDateTime createTime, LocalDateTime updateTime) {
        this(postId, nickname, title, content, Timestamped.getTimeString(createTime), Timestamped.getTimeString(updateTime));
    }

    public static PostResponseDto of(Post post) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .createTime(Timestamped.getTimeString(post.getCreateTime()))
                .updateTime(Timestamped.getTimeString(post.getUpdateTime()))
                .build();
    }
}
