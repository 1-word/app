package com.numo.api.domain.wordbook.word.dto;

public record WordCountDto(
        Long totalCount,
        Long memorizedCount,
        Long unMemorizedCount
) {
}
