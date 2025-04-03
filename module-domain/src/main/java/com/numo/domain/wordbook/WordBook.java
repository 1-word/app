package com.numo.domain.wordbook;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.dto.WordBookUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class WordBook extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_book_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    //230923 폴더 컬러 추가
    private String background;
    private String color;
    private String memo;

    @Embedded
    private WordCount wordCount;

    @Column(unique = true)
    private String link;

    private boolean isShared;

    // todo 권한 관련 밸류 객체
    @Enumerated(EnumType.STRING)
    private WordBookRole anyoneBasicRole;

    @Enumerated(EnumType.STRING)
    private WordBookRole memberBasicRole;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wordBook", cascade = CascadeType.ALL, orphanRemoval = true)
    List<WordBookMember> wordBookMembers;

    @Builder
    public WordBook(Long id, User user, String name, String background, String color, String memo, int totalCount, int memorizedCount, int unMemorizedCount, String link, boolean isShared, WordBookRole anyoneBasicRole, WordBookRole memberBasicRole) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.background = background;
        this.color = color;
        this.memo = memo;
        this.wordCount = new WordCount(totalCount, memorizedCount, unMemorizedCount);
        this.link = link;
        this.isShared = isShared;
        this.anyoneBasicRole = WordBookRole.view;
        this.memberBasicRole = WordBookRole.view;
    }

    public void update(WordBookUpdateDto updateDto) {
        this.name = updateDto.name();
        this.color = updateDto.color();
        this.background = updateDto.background();
        this.memo = updateDto.memo();
    }

    public void removeMember() {
        wordBookMembers.clear();
    }

    public boolean isDeleteAllowed() {
        return wordCount.isDeleteAllowed();
    }

    /**
     * 단어 삭제 시 count 삭제
     */
    public void deleteCount(String memorization) {
        wordCount = wordCount.decrementCount(memorization);
    }

    /**
     * 암기 여부가 변경되었을 때 단어 수 업데이트
     * 변경되지 않았으면 단어 수를 업데이트 하지 않는다.
     * @param prevMemorization 단어 암기 여부
     * @param memorization 변경된 단어 암기 여부
     */
    public void updateMemorizationCount(String prevMemorization, String memorization) {
        if (Objects.equals(prevMemorization, memorization)) {
            return;
        }
        wordCount = wordCount.updateCountByMemorizationStatus(memorization);
    }

    /**
     * 단어 추가 시 count 추가
     */
    public void saveWord(String memorization) {
        wordCount = wordCount.incrementCount(memorization);
    }

    public void startSharing() {
        isShared = true;
    }

    public void cancelSharing() {
        isShared = false;
    }

    public boolean isOwner(Long userId) {
        return Objects.equals(userId, user.getUserId());
    }

    public int getTotalCount() {
        return wordCount.getTotalCount();
    }

    public int getMemorizedCount() {
        return wordCount.getTotalCount();
    }

    public int getUnMemorizedCount() {
        return wordCount.getTotalCount();
    }
}
