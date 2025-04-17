package com.numo.api.domain.wordbook.dto;

import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.WordBookRole;
import lombok.Builder;

@Builder
public record ShareWordBookResponseDto(
    Long wordBookId,
    String nickname,
    String name,
    String memo,
    String color,
    String background,
    WordBookRole role,
    int totalCount,
    int memorizedCount,
    int unMemorizedCount
) {
    public static ShareWordBookResponseDto of(WordBook wordBook) {
        return ShareWordBookResponseDto.builder()
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
