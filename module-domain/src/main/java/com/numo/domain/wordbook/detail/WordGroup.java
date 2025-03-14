package com.numo.domain.wordbook.detail;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.detail.dto.WordGroupRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class WordGroup extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordGroupId;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    private String name;
    private String description;

    @ColumnDefault("'N'")
    private String defaultGroup;

    public void update(WordGroupRequestDto requestDto) {
        this.name = requestDto.name();
        this.description = requestDto.description();
    }

    public boolean isDefaultGroup() {
       return Objects.equals("Y", defaultGroup);
    }
}
