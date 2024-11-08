package com.numo.wordapp.model.word;

import com.numo.wordapp.model.Timestamped;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sound")
public class Sound extends Timestamped {
    @Id
    private String word; //기본키

    @Column(name = "sound_path")
    private String soundPath; //유의어
    private String memo;    //메모

    @Builder
    public Sound(String word, String soundPath, String memo){
        this.word = word;
        this.memo = memo;
        this.soundPath = soundPath;
    }
}
