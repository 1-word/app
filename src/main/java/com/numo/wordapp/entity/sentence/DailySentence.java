package com.numo.wordapp.entity.sentence;

import com.numo.wordapp.entity.Timestamped;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.entity.word.WordDailySentence;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

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

    public void setWordDailySentence(List<Word> words) {
        wordDailySentences = new ArrayList<>();
        DailySentence dailySentence = this;
        for (Word word : words) {
            WordDailySentence wordDailySentence = new WordDailySentence(word, dailySentence);
            wordDailySentences.add(wordDailySentence);
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
