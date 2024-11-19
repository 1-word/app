package com.numo.wordapp.dto.word.detail;

import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.entity.word.detail.WordDetail;
import com.numo.wordapp.entity.word.detail.WordGroup;
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
