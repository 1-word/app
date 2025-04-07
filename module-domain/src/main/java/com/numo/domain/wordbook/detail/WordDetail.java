package com.numo.domain.wordbook.detail;

import com.numo.domain.base.Timestamped;
import com.numo.domain.wordbook.detail.dto.UpdateWordDetailDto;
import com.numo.domain.wordbook.word.Word;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class WordDetail extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordDetailId;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne
    @JoinColumn(name = "word_group_id")
    private WordGroup wordGroup;

    private String title;
    private String content;

    public void addWord(Word word) {
        this.word = word;
    }

    public void update(UpdateWordDetailDto detailDto) {
        this.title = detailDto.title();
        this.content = detailDto.content();
        this.wordGroup = WordGroup.builder()
                .wordGroupId(detailDto.wordGroupId())
                .build();
    }

}
