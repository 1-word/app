package com.numo.api.domain.wordbook.detail.dto;

import com.numo.domain.word.Word;
import com.numo.domain.word.detail.WordDetail;
import com.numo.domain.word.detail.WordGroup;
import lombok.Builder;

@Builder
public record WordDetailRequestDto(
        Long wordId,
        Long wordGroupId,
        String title,
        String content
) {
    public WordDetail toEntity() {
        Word word = Word.builder().wordId(wordId).build();
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
