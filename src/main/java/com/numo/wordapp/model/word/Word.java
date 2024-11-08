package com.numo.wordapp.model.word;

import com.numo.wordapp.model.Timestamped;
import com.numo.wordapp.model.word.detail.WordDetailMain;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Data //롬복의 Data 애노테이션 사용시, toString이 포함되어있어 무한참조가 발생하게 된다. 오버라이딩하거나 사용하지 않음으로 처리
@Entity
@Getter
@Setter
@NoArgsConstructor
//@DynamicUpdate //변경한 필드만 업데이트
@Table(name = "word_master")
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public class Word extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment로 값 지정.
    @Column(name = "word_id")
    private int wordId;    //기본키

    @Column(name = "user_id")
    private String userId;

    private String word;    //단어
    private String mean;    //뜻
    @Column(name = "`read`")
    private String read;   //읽는법
    private String memo;    //메모
    private String soundPath;   //230423추가 발음 파일 경로
    private String memorization;    //230724추가 암기여부 Y/N

    @Enumerated(EnumType.STRING)
    private GttsCode lang;   //20230930추가 단어 타입 (영어, 일본어 등)

//    @Column(name = "folder_id")
//    private Integer folderId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="folder_id")
    private Folder folder;

    //양방향 관계, 단어가 삭제되면 유의어도 삭제되도록 CascadeType.REMOVE속성 사용.
    //@OneToMany(mappedBy = "word", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OneToMany(mappedBy = "word", // fk인스턴스
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<WordDetailMain> wordDetailMains = new ArrayList<>(); //초기화 선언

    @Builder
    public Word(String userId, String word, String mean, String read, String memo, String memorization, Integer folderId, GttsCode lang){
        this.userId = userId;
        this.word = word;
        this.mean = mean;
        this.read = read;
        this.memo = memo;
        this.memorization = memorization;
//        this.folderId = folderId;
        this.lang = lang;
    }

    // 연관관계 설정
    // word는 연관관계의 주인이 아님, 메소드를 통해 데이터 삽입해준다.
    public void addWordDetailMain(WordDetailMain wordDetailMain){
        this.wordDetailMains.add(wordDetailMain);
        if(wordDetailMain.getWord() != this){
            wordDetailMain.setWord(this);
        }
    }

//    public void addWordDetailMain(List<WordDetailMain> wordDetailMains){
//        this.wordDetailMains = wordDetailMains;
//        for (WordDetailMain wordDetailMain : wordDetailMains) {
//            if (wordDetailMain.getWord() != this) {
//                wordDetailMain.setWord(this);
//            }
//        }
//    }

    public void setFolderId(Integer id){
        folder.setFolderId(id);
    }

    public int getFolderId(){
        return folder.getFolderId();
    }
}
