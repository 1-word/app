package com.numo.api.domain.wordbook.detail.dto.read;

import java.time.LocalDateTime;

public record ReadWordDetailResponseDto(
        Long wordDetailId,
        String title,
        String content,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
