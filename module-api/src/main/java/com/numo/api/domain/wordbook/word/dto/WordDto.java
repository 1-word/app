package com.numo.api.domain.wordbook.word.dto;

import com.numo.domain.wordbook.sound.type.GttsCode;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WordDto(
        Long wordId,
        @Deprecated
        Long folderId,
        Long wordBookId,
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
    public WordDto {
        folderId = wordBookId;
    }
}
