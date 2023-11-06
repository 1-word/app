package com.numo.wordapp.model.word.detail;

import com.numo.wordapp.model.Timestamped;
import com.numo.wordapp.model.word.Word;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "word_detail_main")
public class WordDetailMain extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_detail_main_id")
    private int detailMainId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "word_id")
    private Word word;

    @OneToOne()
    @JoinColumn(name = "title_id")
    private WordDetailTitle wordDetailTitle;

    @OneToMany(mappedBy = "wordDetailMain",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<WordDetailSub> wordDetailSub = new ArrayList<>();

    private String content;
    private String memo;

    @Builder
    public WordDetailMain(int detailMainId, String content, String memo, Word word, List<WordDetailSub> wordDetailSub, WordDetailTitle wordDetailTitle){
        this.detailMainId = detailMainId;
        this.content = content;
        this.memo = memo;
        this.word = word;
        this.wordDetailSub = wordDetailSub;
        this.wordDetailTitle = wordDetailTitle;
    }

    public void addWordDetailSub(WordDetailSub wordDetailSub) {
//        this.wordDetailSub.add(wordDetailSub);
        if (wordDetailSub.getWordDetailMain() != this) {
            wordDetailSub.setWordDetailMain(this);
        }
    }
}
