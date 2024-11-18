package com.numo.wordapp.entity.word;

import com.numo.wordapp.dto.word.UpdateWordDto;
import com.numo.wordapp.dto.word.detail.WordDetailRequestDto;
import com.numo.wordapp.entity.Timestamped;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.entity.word.detail.WordDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
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

    @OneToOne
    private Sound sound;

    @ColumnDefault("'N'")
    private String memorization;    //230724추가 암기여부 Y/N

    @Enumerated(EnumType.STRING)
    private GttsCode lang;   //20230930추가 단어 타입 (영어, 일본어 등)

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="folder_id")
    private Folder folder;

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WordDetail> wordDetails = new ArrayList<>();

    public void setWordDetails() {
        addWordDetails(wordDetails);
    }

    public void addWordDetails(List<WordDetail> wordDetails) {
        for (WordDetail wordDetail : wordDetails) {
            addWordDetail(wordDetail);
        }
    }

    private void addWordDetail(WordDetail wordDetail) {
        wordDetails.add(wordDetail);
        if (wordDetail.getWord() != this) {
            wordDetail.addWord(this);
        }
    }

    public void updateWord(UpdateWordDto dto) {
        mean = dto.mean();
        read = dto.read();
        updateWordDetails(dto.details());
    }

    private void updateWordDetails(List<WordDetailRequestDto> detailsDto) {
        int detailsSize = wordDetails.size();
        int requestSize = detailsDto.size();
        int index = 0;

        for (WordDetailRequestDto detailDto : detailsDto) {
            if (detailsSize == requestSize) {
                WordDetail wordDetail = wordDetails.get(index);
                wordDetail.update(detailDto);
            // 원래 있던 데이터보다 많으면 새로운 객체를 만들어야한다.
            } else if (detailsSize < requestSize) {
                wordDetails.add(detailDto.toEntity());
            //원래 있던 데이터보다 적으면 객체를 삭제해주어야 한다. -> 그냥 제일 마지막 객체를 삭제하는걸로....
            } else {
                int diffCount = detailsSize - requestSize;
                for (int i = 1; i <= diffCount; i++) {
                    wordDetails.remove(detailsSize - i);
                }
            }
            index++;
        }
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateMemorization(String memorization) {
        this.memorization = memorization;
    }

    public void setFolder(Long folderId) {
        folder = Folder.builder()
                .folderId(folderId)
                .build();
    }

    public Long getFolderId(){
        return folder.getFolderId();
    }
}
