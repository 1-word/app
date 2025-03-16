package com.numo.domain.shareroom;

import com.numo.domain.base.Timestamped;
import com.numo.domain.wordbook.WordBook;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class ShareRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_book_id")
    private WordBook wordBook;

    public boolean isOwner(Long userId) {
        return wordBook.isOwner(userId);
    }
}
