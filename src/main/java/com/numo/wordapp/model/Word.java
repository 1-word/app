package com.numo.wordapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "word")
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class Word extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int word_id;    //기본키

    private String word;    //단어
    private String mean;    //뜻
    private String wread;   //읽는법
    private String memo;    //메모

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Synonym> synonyms = new ArrayList<Synonym>();

    /*@CreatedDate
    private LocalDateTime create_time;   //생성 날짜

    @LastModifiedDate
    private LocalDateTime update_time;     //업데이트 날짜*/

}
