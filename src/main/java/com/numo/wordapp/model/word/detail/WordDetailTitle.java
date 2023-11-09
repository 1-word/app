package com.numo.wordapp.model.word.detail;

import com.numo.wordapp.model.Timestamped;
import com.numo.wordapp.security.model.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "word_detail_title")
public class WordDetailTitle extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int titleId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "title_type")
    private TitleCode titleType;

    @Column(name = "title_name")
    private String titleName;

    private String memo;

    @Builder
    public WordDetailTitle(int titleId, User user, String titleType, String titleName, String memo) {
        this.titleId = titleId;
        this.user = user;
//        this.titleType = TitleCode.valueOf(titleType);
        this.titleName = titleName;
        this.memo = memo;
    }

    public WordDetailTitle(int titleId){
        this.titleId = titleId;
    }

//    public String getTitleType(){
//        if(titleType != null) {
//            return this.titleType.name();
//        }
//        return "";
//    }


}
