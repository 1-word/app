package com.numo.api.domain.wordbook.word.dto;

import com.numo.domain.word.sound.type.GttsCode;
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
