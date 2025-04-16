package com.numo.domain.wordbook.word;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class WordHistory extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_history_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    private Long wordBookId;

    @Column(columnDefinition = "TEXT")
    @Comment("단어의 저장 전의 json 데이터")
    private String beforeData;

    @Column(columnDefinition = "TEXT")
    @Comment("단어의 저장 이후 json 데이터")
    private String afterData;

    @ManyToOne
    private User modifiedBy;

    private boolean isRestored;

    @Builder
    public WordHistory(Long id, Operation operation, Word word, String beforeData, String afterData, User modifiedBy, Boolean isRestored) {
        this.id = id;
        this.operation = operation;
        this.word = word;
        this.wordBookId = word.getWordBookId();
        this.beforeData = beforeData;
        this.afterData = afterData;
        this.modifiedBy = modifiedBy;
        this.isRestored = isRestored != null && isRestored;
    }

    public WordHistory createRestoreHistory() {
        switch (operation) {
            case INSERT -> operation = Operation.DELETE;
            case DELETE -> operation = Operation.INSERT;
            case UPDATE -> {}
        }
        // 데이터 되돌리기
        return WordHistory.builder()
                .operation(operation)
                .word(word)
                .beforeData(afterData)
                .afterData(beforeData)
                .isRestored(true)
                .build();
    }

    public enum Operation {
        INSERT, UPDATE, DELETE
    }
}
