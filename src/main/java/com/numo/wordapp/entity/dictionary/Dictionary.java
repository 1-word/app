package com.numo.wordapp.entity.dictionary;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictId;
    private String word;
    private String wordType;
    @Column(columnDefinition="TEXT")
    private String definition;
}
