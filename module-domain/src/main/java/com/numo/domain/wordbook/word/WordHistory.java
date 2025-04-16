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

    private Long wordId;
    private Long wordBookId;

    @Column(columnDefinition = "TEXT")
    @Comment("단어의 저장 전의 json 데이터")
    private String beforeData;

    @Column(columnDefinition = "TEXT")
    @Comment("단어의 저장 이후 json 데이터")
    private String afterData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    private boolean isRestored;

    private Long restoreId;

    @Builder
    public WordHistory(Long id, Operation operation, Long wordId, Long wordBookId, String beforeData, String afterData, User modifiedBy, Boolean isRestored, Long restoreId) {
        this.id = id;
        this.operation = operation;
        this.wordId = wordId;
        this.wordBookId = wordBookId;
        this.beforeData = beforeData;
        this.afterData = afterData;
        this.modifiedBy = modifiedBy;
        this.isRestored = isRestored != null && isRestored;
        this.restoreId = restoreId;
    }

    public WordHistory createRestoreHistory(User modifiedBy) {
        isRestored = true;
        Operation newOperation = operation;
        switch (operation) {
            case INSERT -> newOperation = Operation.DELETE;
            case DELETE -> newOperation = Operation.INSERT;
            case UPDATE -> {}
        }
        // INSERT의 경우 이전 데이터가 없으므로 현재 데이터를 넣어준다.
        String newBeforeData = beforeData;
        if (beforeData == null) {
            newBeforeData = afterData;
        }
        Long newRestoreId = restoreId == null? id : restoreId;
        // 데이터 되돌리기
        return WordHistory.builder()
                .operation(newOperation)
                .wordBookId(wordBookId)
                .wordId(wordId)
                .beforeData(afterData)
                .afterData(newBeforeData)
                .modifiedBy(modifiedBy)
                .restoreId(newRestoreId)
                .build();
    }

    public void update(Long wordId, String afterData) {
        this.wordId = wordId;
        this.afterData = afterData;
    }

    public enum Operation {
        INSERT, UPDATE, DELETE
    }
}
