package com.numo.domain.quiz;

import com.numo.domain.wordbook.word.Word;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_info_id")
    QuizInfo quizInfo;

    @ManyToOne
    @JoinColumn(name = "word_id")
    Word word;

    @Column(columnDefinition="int(1)")
    Integer correct;

    public void setCorrect(boolean correct) {
        this.correct = correct? 1 : 0;
    }
}
