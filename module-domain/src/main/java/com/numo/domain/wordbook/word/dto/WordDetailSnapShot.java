package com.numo.domain.wordbook.word.dto;

import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.detail.WordGroup;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class WordDetailSnapShot {
    private Long wordDetailId;
    private Long wordGroupId;
    private String title;
    private String content;

    public static WordDetailSnapShot copyOf(WordDetail wordDetail) {
        return WordDetailSnapShot.builder()
                .wordDetailId(wordDetail.getWordDetailId())
                .wordGroupId(wordDetail.getWordGroup().getWordGroupId())
                .title(wordDetail.getTitle())
                .content(wordDetail.getContent())
                .build();
    }

    public WordDetail toEntity() {
        WordGroup wordGroup = new WordGroup(wordGroupId);
        return WordDetail.builder()
                .wordDetailId(wordDetailId)
                .wordGroup(wordGroup)
                .title(title)
                .content(content)
                .build();
    }

    public WordDetail toInsertEntity() {
        WordGroup wordGroup = new WordGroup(wordGroupId);
        return WordDetail.builder()
                .wordGroup(wordGroup)
                .title(title)
                .content(content)
                .build();
    }
}
