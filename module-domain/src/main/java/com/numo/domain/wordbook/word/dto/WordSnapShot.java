package com.numo.domain.wordbook.word.dto;

import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.WordHistory;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class WordSnapShot {
    private Long wordBookId;
    private Long wordId;
    private Long userId;
    private Long soundId;
    private String word;
    private String mean;
    private String read;
    private String memo;
    private String memorization;
    private GttsCode lang;
    private List<WordDetailSnapShot> wordDetails;

    public static WordSnapShot copyOf(Word word) {
        List<WordDetailSnapShot> detailSnapShot = word.getWordDetails().stream().map(WordDetailSnapShot::copyOf).toList();
        return WordSnapShot.builder()
                .wordBookId(word.getWordBookId())
                .wordId(word.getWordId())
                .userId(word.getUser().getUserId())
                .soundId(word.getSound().getSoundId())
                .word(word.getWord())
                .mean(word.getMean())
                .read(word.getRead())
                .memo(word.getMemo())
                .memorization(word.getMemorization())
                .lang(word.getLang())
                .wordDetails(detailSnapShot)
                .build();
    }

    public Word toEntity(WordHistory.Operation operation) {
        User user = new User(userId);
        Sound sound = new Sound(soundId);
        WordBook wordBook = new WordBook(wordBookId);
        List<WordDetail> wordDetail = getWordDetails(operation);
        return Word.builder()
                .wordId(getWordId(operation))
                .user(user)
                .wordBook(wordBook)
                .sound(sound)
                .wordDetails(wordDetail)
                .word(word)
                .mean(mean)
                .read(read)
                .memo(memo)
                .memorization(memorization)
                .lang(lang)
                .build();
    }

    private Long getWordId(WordHistory.Operation operation) {
        if (operation == WordHistory.Operation.INSERT) {
            return null;
        }
        return wordId;
    }

    private List<WordDetail> getWordDetails(WordHistory.Operation operation) {
        if (operation == WordHistory.Operation.INSERT) {
            return wordDetails.stream().map(WordDetailSnapShot::toInsertEntity).toList();
        }
        return wordDetails.stream().map(WordDetailSnapShot::toEntity).toList();
    }

}
