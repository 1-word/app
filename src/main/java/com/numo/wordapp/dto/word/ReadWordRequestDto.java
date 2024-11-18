package com.numo.wordapp.dto.word;

import lombok.Builder;

@Builder
public record ReadWordRequestDto(
        Long folderId,
        String searchText,
        String memorization,
        String lang
) {
    public static ReadWordRequestDto of(SearchWordRequestDto requestDto) {
        return of(null, requestDto);
    }

    public static ReadWordRequestDto of(String searchText, SearchWordRequestDto requestDto) {
        return ReadWordRequestDto.builder()
                .searchText(searchText)
                .folderId(requestDto.folderId())
                .memorization(requestDto.memorization())
                .lang(requestDto.language())
                .build();

    }
}
