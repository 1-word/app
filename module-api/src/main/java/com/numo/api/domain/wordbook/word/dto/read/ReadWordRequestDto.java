package com.numo.api.domain.wordbook.word.dto.read;

import com.numo.api.domain.wordbook.word.dto.search.SearchWordRequestDto;
import com.numo.domain.wordbook.type.SortType;
import lombok.Builder;

@Builder
public record ReadWordRequestDto(
        @Deprecated
        Long folderId,
        Long wordBookId,
        String searchText,
        String memorization,
        String lang,
        SortType sort,
        String seed
) {
    public ReadWordRequestDto {
        if (folderId != null) {
            wordBookId = folderId();
        }
    }

    public static ReadWordRequestDto of(SearchWordRequestDto requestDto) {
        return of(null, requestDto);
    }

    public static ReadWordRequestDto of(String searchText, SearchWordRequestDto requestDto) {
        return ReadWordRequestDto.builder()
                .searchText(searchText)
                .wordBookId(requestDto.wordBookId())
                .memorization(requestDto.memorization())
                .lang(requestDto.language())
                .sort(requestDto.sort())
                .seed(requestDto.seed())
                .build();

    }
}
