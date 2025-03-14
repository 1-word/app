package com.numo.domain.wordbook.detail.dto;

import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.detail.WordGroup;
import lombok.Builder;

@Builder
public record UpdateWordDetailDto(
        Long wordDetailId,
        Long wordGroupId,
        String title,
        String content
) {
    public WordDetail toEntity(Word word) {
        WordGroup wordGroup = WordGroup.builder()
                .wordGroupId(wordGroupId)
                .build();
        return WordDetail.builder()
                .word(word)
                .wordGroup(wordGroup)
                .title(title)
                .content(content)
                .build();
    }
}
