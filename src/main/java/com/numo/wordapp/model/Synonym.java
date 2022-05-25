package com.numo.wordapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.numo.wordapp.dto.SynonymDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "synonym")
public class Synonym extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int synonym_id; //기본키


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    //@JsonIgnore //무한 참조 방지 애노테이션
    private Word word; //외래키

    private String synonym; //유의어
    private String memo;    //메모

    @Builder
    public Synonym(String synonym, String memo, Word word){
        this.synonym = synonym;
        this.memo = memo;
        this.word = word;
    }
    public Synonym(SynonymDto.Request synonymDto) {
        //this.synonym_id = synonymDto.toEntity().getSynonym_id();
        //this.word = synonymDto.toEntity().getWord();
        this.synonym = synonymDto.toEntity().getSynonym();
        this.memo = synonymDto.toEntity().getMemo();
    }

    public static Synonym createSynonym(SynonymDto.Request synonymDto, Word word){
        Synonym synonym = synonymDto.toEntity();
        return Synonym.builder()
                .synonym(synonym.getSynonym())
                .memo(synonym.getMemo())
                .word(word)
                .build();
    }
    //private int word_id;
    //private String update_time;   //업데이트 날짜
    //private String create_time;   //생성 날짜

   /* public void addWord(Word word){
        if(this.word != null){
            this.word.getSynonyms().remove(this);
        }
        this.word = word;
        word.getSynonyms().add(this);
    }
    */
    public void addWord(Word word){
        this.word = word;
        if(!word.getSynonyms().contains(this)){
            word.getSynonyms().add(this);
        }
    }
}
