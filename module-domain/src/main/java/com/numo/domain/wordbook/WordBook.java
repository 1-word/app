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

    private int totalCount;
    private int memorizedCount;
    private int unMemorizedCount;

    @Column(unique = true)
    private String link;

    private boolean isShared;

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
        this.totalCount = totalCount;
        this.memorizedCount = memorizedCount;
        this.unMemorizedCount = unMemorizedCount;
        this.link = link;
        this.isShared = isShared;
        this.anyoneBasicRole = WordBookRole.view;
        this.memberBasicRole = WordBookRole.view;
    }

    public boolean hasReadPermission(Long userId) {
        if (isOwner(userId)) {
            return true;
        }

        for (WordBookMember wordBookMember : wordBookMembers) {
            if (wordBookMember.isMember(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean update(Long userId, WordBookUpdateDto updateDto) {
        if (!isOwner(userId)) {
            return false;
        }
        this.name = updateDto.name();
        this.color = updateDto.color();
        this.background = updateDto.background();
        this.memo = updateDto.memo();
        return true;
    }

    public void removeMember() {
        wordBookMembers.clear();
    }

    public boolean isDeleteAllowed() {
        return totalCount <= 0;
    }

    /**
     * 단어 삭제 시 count 삭제
     */
    public void deleteWord(WordCountType type) {
        switch (type) {
            case memorized -> this.memorizedCount--;
            case unmemorized -> this.unMemorizedCount--;
        }
        this.totalCount--;
    }

    /**
     * 단어 추가 시 count 추가
     */
    public void saveWord(WordCountType type) {
        switch (type) {
            case memorized -> this.memorizedCount++;
            case unmemorized -> this.unMemorizedCount++;
        }
        this.totalCount++;
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

}
