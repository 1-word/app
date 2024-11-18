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
    private String soundPath; //유의어
    private String memo;    //메모

    public boolean hasSound() {
        return soundPath != null && !soundPath.isEmpty();
    }
}
