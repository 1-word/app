package com.numo.wordapp.entity.word.detail;

import com.numo.wordapp.entity.Timestamped;
import com.numo.wordapp.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

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
    @JoinColumn(name = "user_id")
    private User user;

    @Max(20)
    private String name;
    @Max(100)
    private String description;
}
