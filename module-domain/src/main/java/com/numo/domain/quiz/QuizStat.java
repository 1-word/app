package com.numo.domain.quiz;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import com.numo.domain.base.BaseDate;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    private User user;

    private int totalCount;
    private int correctCount;
    private int wrongCount;

    @Embedded
    private BaseDate baseDate;

    @Builder
    public QuizStat(Long id, QuizInfo quizInfo, int totalCount, int correctCount, int wrongCount, User user) {
        this.id = id;
        this.quizInfo = quizInfo;
        this.totalCount = totalCount;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.user = user;
        this.baseDate = new BaseDate();
    }

}
