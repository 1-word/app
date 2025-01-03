package com.numo.domain.sentence;

import com.numo.domain.Timestamped;
import com.numo.domain.sentence.dto.CreateWordDailySentenceDto;
import com.numo.domain.sentence.dto.DailySentenceRequestDto;
import com.numo.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
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
    private int year;
    private int month;
    private int week;
    private int day;

    @Builder
    public DailySentence(Long dailySentenceId, User user, String sentence, String mean) {
        this.dailySentenceId = dailySentenceId;
        this.user = user;
        this.sentence = sentence;
        this.mean = mean;
        setDate();
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

    private void setDate() {
        LocalDate localDate = LocalDate.now();
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();
        this.week = getCurrentWeekOfMonth(localDate);
    }

    private int getCurrentWeekOfMonth(LocalDate localDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);

        int weekOfMonth = localDate.get(weekFields.weekOfMonth());

        // 첫 주에 해당하지 않는 주의 경우 1주차로 계산
        if (weekOfMonth == 0) {
            weekOfMonth++;
        }

        return weekOfMonth;
    }
}
