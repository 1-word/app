package com.numo.wordapp.entity.word;

import com.numo.wordapp.entity.Timestamped;
import lombok.*;

import jakarta.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Sound extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long soundId;

    @Column(unique = true)
    private String word;
    private String memo;    //메모

}
