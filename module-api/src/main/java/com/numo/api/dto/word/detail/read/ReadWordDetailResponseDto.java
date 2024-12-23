package com.numo.api.dto.word.detail.read;

import java.time.LocalDateTime;

public record ReadWordDetailResponseDto(
        Long wordDetailId,
        String title,
        String content,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
