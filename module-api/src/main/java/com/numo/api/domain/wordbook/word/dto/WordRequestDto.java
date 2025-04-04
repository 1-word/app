package com.numo.api.domain.wordbook.word.dto;

import com.numo.api.domain.wordbook.detail.dto.WordDetailRequestDto;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.word.Word;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record WordRequestDto(
        @Deprecated
        Long wordBookId,
        @NotNull
        String word,
        String mean,
        String read,
        String memo,
        String memorization,
        List<WordDetailRequestDto> details
) {
    public WordRequestDto {
        word = word.replaceAll("\\s", "");
    }

    public Word toEntity(User user, Sound sound, WordBook wordBook, GttsCode gttsType) {
        List<WordDetail> wordDetails = details.stream().map(WordDetailRequestDto::toEntity).toList();
        return Word.builder()
                .user(user)
                .sound(sound)
                .wordbook(wordBook)
                .wordDetails(wordDetails)
                .word(word)
                .mean(mean)
                .read(read)
                .memo(memo)
                .memorization(memorization)
                .lang(gttsType)
                .build();
    }

}
