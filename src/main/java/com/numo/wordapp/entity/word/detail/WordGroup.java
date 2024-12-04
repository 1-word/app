package com.numo.wordapp.entity.word.detail;

import com.numo.wordapp.dto.word.group.WordGroupRequestDto;
import com.numo.wordapp.entity.Timestamped;
import com.numo.wordapp.entity.user.User;
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
