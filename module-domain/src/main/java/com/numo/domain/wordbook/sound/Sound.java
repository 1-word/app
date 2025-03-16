package com.numo.domain.wordbook.sound;

import com.numo.domain.base.Timestamped;
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
