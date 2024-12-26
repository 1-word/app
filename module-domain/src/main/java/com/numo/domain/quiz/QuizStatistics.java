package com.numo.domain.quiz;

import com.numo.domain.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class QuizStatistics extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @ManyToOne
    QuizInfo quizInfo;

    private int correctCount;
    private int wrongCount;
    private int year;
    private int month;
    private int week;
    private int day;
}
