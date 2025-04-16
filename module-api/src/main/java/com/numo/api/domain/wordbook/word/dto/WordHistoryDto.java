package com.numo.api.domain.wordbook.word.dto;

import com.numo.domain.base.Timestamped;
import com.numo.domain.wordbook.word.WordHistory;

import java.time.LocalDateTime;

public record WordHistoryDto(
        Long wordHistoryId,
        WordHistory.Operation operation,
        String beforeData,
        String afterData,
        String nickname,
        String profileImagePath,
        String createTime,
        String updateTime
) {
    public WordHistoryDto(Long wordHistoryId, WordHistory.Operation operation, String beforeData, String afterData, String nickname, String profileImagePath, LocalDateTime createTime, LocalDateTime updateTime) {
        this(
                wordHistoryId,
                operation,
                beforeData,
                afterData,
                nickname,
                profileImagePath,
                Timestamped.getTimeString(createTime),
                Timestamped.getTimeString(updateTime)
        );
    }


}
