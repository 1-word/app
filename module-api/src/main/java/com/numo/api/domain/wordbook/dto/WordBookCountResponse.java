package com.numo.api.domain.wordbook.dto;

public interface WordBookCountResponse {
        Long getWordBookId();
        String getNickname();
        String getName();
        String getMemo();
        String getColor();
        String getBackground();
        Long getTotalCount();
        Long getMemorizedCount();
        Long getUnMemorizedCount();


}
