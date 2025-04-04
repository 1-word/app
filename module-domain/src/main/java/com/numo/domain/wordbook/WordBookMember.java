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

    public boolean hasReadPermission() {
        return role.hasViewPermission();
    }

    public boolean hasWritePermission() {
        return role.hasEditPermission();
    }

    public boolean hasAdminPermission() {
        return role.hasAdminPermission();
    }

    public void setRole(WordBookRole role) {
        this.role = role;
    }

}
