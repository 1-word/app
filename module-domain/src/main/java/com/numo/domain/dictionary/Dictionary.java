package com.numo.domain.dictionary;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictId;
    private String word;
    private String wordType;
    @Column(columnDefinition="TEXT")
    private String definition;
    @Column(columnDefinition="TEXT")
    private String mean;
    private String isCrawling;

    @Builder
    public Dictionary(Long dictId, String word, String wordType, String definition, String mean, String isCrawling) {
        this.dictId = dictId;
        this.word = word.toLowerCase();
        this.wordType = wordType;
        this.definition = definition;
        this.mean = mean;
        this.isCrawling = isCrawling;
    }

    public boolean checkCrawling() {
        return !Objects.isNull(isCrawling);
    }

    public void updateMean(String mean) {
        this.mean = mean;
        this.isCrawling = "Y";
    }

    public boolean isRealWord(String searchText) {
        return Objects.equals(word.toLowerCase(), searchText.toLowerCase());
    }
}
