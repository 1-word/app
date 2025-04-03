package com.numo.domain.wordbook;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class WordBookMember extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_book_id")
    private WordBook wordBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private WordBookRole role;

    public boolean hasReadPermission(Long userId) {
        return checkRole(WordBookRole.view, userId);
    }

    public boolean hasWritePermission(Long userId) {
        return checkRole(WordBookRole.edit, userId);
    }

    public boolean hasAdminPermission(Long userId) {
        return checkRole(WordBookRole.admin, userId);
    }

    private boolean checkRole(WordBookRole role, Long userId) {
        return switch (role) {
            case view -> this.role.hasViewPermission();
            case edit -> this.role.hasEditPermission();
            case admin -> this.role.hasAdminPermission();
        };
    }

    public void setRole(WordBookRole role) {
        this.role = role;
    }

}
