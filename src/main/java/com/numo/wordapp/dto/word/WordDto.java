package com.numo.wordapp.dto.word;

import com.numo.wordapp.entity.word.GttsCode;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WordDto(
        Long wordId,
        Long folderId,
        String word,
        String mean,
        String read,
        String memo,
        String soundPath,
        String memorization,
        GttsCode lang,
        LocalDateTime updateTime,
        LocalDateTime createTime
        ) {
}
