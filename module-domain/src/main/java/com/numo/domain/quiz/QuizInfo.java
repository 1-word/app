package com.numo.domain.quiz;

import com.numo.domain.base.Timestamped;
import com.numo.domain.quiz.type.QuizType;
import com.numo.domain.user.User;
import com.numo.domain.word.folder.Folder;
import com.numo.domain.word.type.SortType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class QuizInfo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_info_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    Folder folder;

    @Column(name = "quiz_type")
    private QuizType type;
    private SortType sort;

    private String memorization;

    @Column(name = "quiz_count")
    private int count;

    private boolean complete;

    public QuizInfo(Long id) {
        this.id = id;
    }

    public void quizComplete() {
        this.complete = true;
    }
}
