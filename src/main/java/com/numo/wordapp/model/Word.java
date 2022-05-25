package com.numo.wordapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.numo.wordapp.dto.WordDto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Data //롬복의 Data 애노테이션 사용시, toString이 포함되어있어 무한참조가 발생하게 된다. 오버라이딩하거나 사용하지 않음으로 처리
@Entity
@Getter
@Setter
@NoArgsConstructor
//@DynamicUpdate //변경한 필드만 업데이트
@Table(name = "word")
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class Word extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment로 값 지정.
    private int word_id;    //기본키

    private String word;    //단어
    private String mean;    //뜻
    private String wread;   //읽는법
    private String memo= "first";    //메모

    //양방향 관계, 단어가 삭제되면 유의어도 삭제되도록 CascadeType.REMOVE속성 사용.
    //@OneToMany(mappedBy = "word", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OneToMany(mappedBy = "word", //연관관계 주인은 synonym이다.
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Synonym> synonyms = new ArrayList<>(); //초기화 선언

    @Builder
    public Word(String word, String mean, String wread, String memo){
        this.word = word;
        this.mean = mean;
        this.wread = wread;
        this.memo = memo;
    }


    /*//단방향 관계 설정
    @ManyToOne
    @JoinColumn(name="word_id")
    private Synonym synonym;
    */
    /*@CreatedDate
    private LocalDateTime create_time;   //생성 날짜

    @LastModifiedDate
    private LocalDateTime update_time;     //업데이트 날짜*/

    public static Word createWord(WordDto.Request dto){
        return Word.builder()
                .word(dto.getWord())
                .mean(dto.getMean())
                .wread(dto.getWread())
                .mean(dto.getMean())
                .build();
    }
    // 연관관계 설정
    // word는 연관관계의 주인이 아님, 그러므로 addSynonym 메소드를 통해 데이터 삽입해준다.
    public void addSynonym(Synonym synonym){
        //System.out.println("[Word]addSynonym: : "+synonym);
        //synonym.setWord(synonym.getWord());
        this.synonyms.add(synonym);
        if(synonym.getWord() != this){
            synonym.setWord(this);
        }
    }

    public void addSynonym(List<Synonym> synonyms){
        this.synonyms = synonyms;
    }

    public void update(Word word){
        this.word = word.getWord();
        this.mean = word.getMean();
        this.wread = word.getWread();
        this.memo = word.getMemo();
    }
}
