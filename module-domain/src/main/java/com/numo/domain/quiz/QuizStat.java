package com.numo.domain.quiz;

import com.numo.domain.base.BaseDate;
import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class QuizStat extends Timestamped {
    @Id
    @Column(name = "quiz_info_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_info_id")
    @MapsId
    QuizInfo quizInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
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
