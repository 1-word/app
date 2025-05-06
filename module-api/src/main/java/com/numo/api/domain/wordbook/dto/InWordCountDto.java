package com.numo.api.domain.wordbook.dto;

public interface InWordCountDto {
    Long getWordBookId();
    Long getTotalCount();
    Long getMemorizedCount();
    Long getUnMemorizedCount();
}