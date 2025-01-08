package com.numo.domain.sentence;

import com.numo.domain.base.BaseDate;
import com.numo.domain.base.Timestamped;
import com.numo.domain.sentence.dto.CreateWordDailySentenceDto;
import com.numo.domain.sentence.dto.DailySentenceRequestDto;
import com.numo.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DailySentence extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailySentenceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "dailySentence", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordDailySentence> wordDailySentences;

    private String sentence;

    @Column(columnDefinition="TEXT")
    private String tagSentence;

    private String mean;

    @Embedded
    private BaseDate baseDate;

    @Builder
    public DailySentence(Long dailySentenceId, User user, String sentence, String mean) {
        this.dailySentenceId = dailySentenceId;
        this.user = user;
        this.sentence = sentence;
        this.mean = mean;
        this.baseDate = new BaseDate();
    }

    public void setTagSentence(String tagSentence) {
        this.tagSentence = tagSentence;
    }

    public void setWordDailySentence(List<CreateWordDailySentenceDto> wordDailySentenceDtos) {
        if (wordDailySentences == null) {
            wordDailySentences = new ArrayList<>();
        }
        DailySentence dailySentence = this;
        for (CreateWordDailySentenceDto wordDailySentenceDto : wordDailySentenceDtos) {
            WordDailySentence wordDailySentence = new WordDailySentence(wordDailySentenceDto.word(),
                    dailySentence,
                    wordDailySentenceDto.matchedWord());
            wordDailySentences.add(wordDailySentence);
        }
    }

    public void update(DailySentenceRequestDto requestDto, List<CreateWordDailySentenceDto> wordDailySentenceDtos, String tagSentence) {
        // 등록된 단어 데이터를 모두 삭제하고 새로 단어 데이터를 등록
        this.sentence = requestDto.sentence();
        this.mean = requestDto.mean();
        this.tagSentence = tagSentence;
        removeDailyWords();
        setWordDailySentence(wordDailySentenceDtos);
    }

    public void update(DailySentenceRequestDto requestDto) {
        this.mean = requestDto.mean();
    }

    public boolean isCurrentSentenceEqual(String sentence) {
        return Objects.equals(sentence, this.sentence);
    }

    public void removeDailyWords() {
        Iterator<WordDailySentence> iterator = wordDailySentences.iterator();
        while(iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

}
