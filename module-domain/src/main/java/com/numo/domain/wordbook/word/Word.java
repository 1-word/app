package com.numo.domain.wordbook.word;

import com.numo.domain.base.Timestamped;
import com.numo.domain.sentence.WordDailySentence;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.detail.dto.UpdateWordDetailDto;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Word extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment로 값 지정.
    private Long wordId;    //기본키

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String word;    //단어
    private String mean;    //뜻
    @Column(name = "`read`")
    private String read;   //읽는법
    private String memo;    //메모

    @ManyToOne
    @JoinColumn(name = "sound_id")
    private Sound sound;

    @ColumnDefault("'N'")
    private String memorization;    //230724추가 암기여부 Y/N

    @Enumerated(EnumType.STRING)
    private GttsCode lang;   //20230930추가 단어 타입 (영어, 일본어 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_book_id")
    private WordBook wordbook;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordDetail> wordDetails;

    // 오늘의 문장 추가
    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordDailySentence> wordDailySentences;

    public Word(Long wordId) {
        this.wordId = wordId;
    }

    public void setWordDetails() {
        addWordDetails(wordDetails);
    }

    public void addWordDetails(List<WordDetail> wordDetails) {
        for (WordDetail wordDetail : wordDetails) {
            addWordDetail(wordDetail);
        }
    }

    private void addWordDetail(WordDetail wordDetail) {
        if (wordDetail.getWord() != this) {
            wordDetail.addWord(this);
        }
    }

    public void updateWord(UpdateWordDto dto) {
        mean = dto.mean();
        read = dto.read();
        setWordbook(dto.folderId());
        updateWordDetails(dto.details());
    }

    private void updateWordDetails(List<UpdateWordDetailDto> detailsDto) {
        int detailsSize = wordDetails.size();
        int requestSize = detailsDto.size();
        int index = 0;

        for (UpdateWordDetailDto detailDto : detailsDto) {
            if (index < detailsSize) {
                WordDetail wordDetail = wordDetails.get(index);
                wordDetail.update(detailDto);
            } else {
                // 원래 있던 데이터보다 많으면 새로운 객체를 만들어야한다.
                Word word = Word.builder().wordId(wordId).build();
                wordDetails.add(detailDto.toEntity(word));
            }
            index++;
        }

        // 원래 있는 데이터보다 수정한 데이터 수가 적으면 삭제
        if (requestSize < detailsSize) {
            int diff = detailsSize - requestSize;
            for (int i = 1; i <= diff; i++) {
                wordDetails.remove(detailsSize - i);
            }
        }
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateMemorization(String prevMemorization, String memorization) {
        wordbook.updateMemorizationCount(prevMemorization, memorization);
        this.memorization = memorization;
    }

    public void addWordBook(WordBook wordbook) {
        wordbook.saveWord(memorization);
        this.wordbook = wordbook;
    }

    @Deprecated
    public void setWordbook(Long wordBookId) {
        if (wordBookId == null) {
            return;
        }
        wordbook = WordBook.builder()
                .id(wordBookId)
                .build();
    }

    public Long getWordBookId(){
        return wordbook.getId();
    }

    public Sound getSound() {
        if (sound == null) {
            return Sound.builder().build();
        }
        return sound;
    }

    public WordBook getWordbook() {
        if (wordbook == null) {
            return WordBook.builder().build();
        }
        return wordbook;
    }

}
