package com.numo.api.domain.wordbook.dto;

import com.numo.domain.wordbook.WordBook;
import lombok.Builder;

@Builder
public record WordBookResponseDto(
    Long wordBookId,
    String nickname,
    String name,
    String memo,
    String color,
    String background,
    int totalCount,
    int memorizedCount,
    int unMemorizedCount
) {
    public static WordBookResponseDto of(WordBook wordBook) {
        return WordBookResponseDto.builder()
                .wordBookId(wordBook.getId())
                .nickname(wordBook.getUser().getNickname())
                .name(wordBook.getName())
                .memo(wordBook.getMemo())
                .color(wordBook.getColor())
                .background(wordBook.getBackground())
                .totalCount(wordBook.getTotalCount())
                .memorizedCount(wordBook.getMemorizedCount())
                .unMemorizedCount(wordBook.getUnMemorizedCount())
                .build();
    }

}
