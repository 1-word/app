package com.numo.domain.quiz;

import com.numo.domain.Timestamped;
import com.numo.domain.util.DateUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class QuizStat extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_stat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_info_id")
    QuizInfo quizInfo;

    private int totalCount;
    private int correctCount;
    private int wrongCount;

    private int year;
    private int month;
    private int week;
    private int day;

    @Builder
    public QuizStat(Long id, QuizInfo quizInfo, int totalCount, int correctCount, int wrongCount) {
        this.id = id;
        this.quizInfo = quizInfo;
        this.totalCount = totalCount;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        setDate();
    }

    private void setDate() {
        LocalDate localDate = LocalDate.now();
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();
        this.week = DateUtil.getCurrentWeekOfMonth(localDate);
    }
}
