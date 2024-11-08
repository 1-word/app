package com.numo.wordapp.model.word;

import com.numo.wordapp.model.Timestamped;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "synonym")
public class Synonym extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int synonym_id; //기본키

    @ManyToOne(optional = true)
    //fetch = FetchType.LAZY,
    @JoinColumn(name = "word_id")
    //@JsonIgnore //무한 참조 방지 애노테이션
    private Word word; //외래키

    private String synonym; //유의어
    private String memo;    //메모

    @Builder
    public Synonym(int synonym_id, String synonym, String memo, Word word){
        this.synonym_id = synonym_id;
        this.synonym = synonym;
        this.memo = memo;
        this.word = word;
    }
}
