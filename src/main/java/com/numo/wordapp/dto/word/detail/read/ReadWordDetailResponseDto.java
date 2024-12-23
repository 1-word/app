package com.numo.wordapp.dto.word.detail.read;

import java.time.LocalDateTime;

public record ReadWordDetailResponseDto(
        Long wordDetailId,
        String title,
        String content,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
