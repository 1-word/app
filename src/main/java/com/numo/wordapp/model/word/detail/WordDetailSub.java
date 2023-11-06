package com.numo.wordapp.model.word.detail;

import com.numo.wordapp.model.Timestamped;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="word_detail_sub")
public class WordDetailSub extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="word_detail_sub_id")
    private int detailSubId;

    @ManyToOne
    @JoinColumn(name="word_detail_main_id")
    private WordDetailMain wordDetailMain;

    @OneToOne()
    @JoinColumn(name="title_id")
    private WordDetailTitle wordDetailTitle;

    private String content;
    private String memo;

    @Builder
    public WordDetailSub(int detailSubId, WordDetailMain wordDetailMain, WordDetailTitle wordDetailTitle, String content, String memo){
        this.detailSubId = detailSubId;
        this.wordDetailMain = wordDetailMain;
        this.wordDetailTitle = wordDetailTitle;
        this.content = content;
        this.memo = memo;
    }


}
