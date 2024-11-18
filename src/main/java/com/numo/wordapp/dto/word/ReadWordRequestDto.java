package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.page.PageDto;
import lombok.Builder;

@Builder
public record ReadWordRequestDto(
        PageDto page,
        Long folderId,
        String searchText,
        String memorization,
        String language
) {
    public static ReadWordRequestDto of(SearchWordRequestDto requestDto) {
        return of(null, requestDto);
    }

    public static ReadWordRequestDto of(String searchText, SearchWordRequestDto requestDto) {
        return ReadWordRequestDto.builder()
                .page(requestDto.page())
                .searchText(searchText)
                .folderId(requestDto.folderId())
                .memorization(requestDto.memorization())
                .language(requestDto.language())
                .build();

    }
}
