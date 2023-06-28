package com.numo.wordapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.numo.wordapp.dto.SynonymDto;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sound")
public class Sound extends Timestamped{
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
