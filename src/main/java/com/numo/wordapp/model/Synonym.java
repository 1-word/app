package com.numo.wordapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "synonym")
public class Synonym extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int synonym_id; //기본키

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word; //외래키

    private String synonym; //유의어
    private String memo;    //메모
    //private int word_id;
    //private String update_time;   //업데이트 날짜
    //private String create_time;   //생성 날짜
}
