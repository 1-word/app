package com.numo.domain.wordbook;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    public boolean isMember(Long userId) {
        return Objects.equals(user.getUserId(), userId);
    }
}
