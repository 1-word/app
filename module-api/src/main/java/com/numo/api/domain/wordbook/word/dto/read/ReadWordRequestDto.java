package com.numo.api.domain.wordbook.word.dto.read;

import com.numo.api.domain.wordbook.word.dto.search.SearchWordRequestDto;
import com.numo.domain.word.type.SortType;
import lombok.Builder;

@Builder
public record ReadWordRequestDto(
        Long folderId,
        String searchText,
        String memorization,
        String lang,
        SortType sort,
        String seed
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
                .sort(requestDto.sort())
                .seed(requestDto.seed())
                .build();

    }
}
