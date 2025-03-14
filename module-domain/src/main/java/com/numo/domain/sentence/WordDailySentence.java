package com.numo.domain.sentence;

import com.numo.domain.wordbook.word.Word;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class WordDailySentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne
    @JoinColumn(name = "daily_sentence_id")
    private DailySentence dailySentence;

    private String matchedWord;

    public WordDailySentence(Word word, DailySentence dailySentence, String matchedWord) {
        this.word = word;
        this.dailySentence = dailySentence;
        this.matchedWord = matchedWord;
    }

}
