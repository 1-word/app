package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.word.detail.WordDetailResponseDto;
import com.numo.wordapp.entity.word.Sound;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.entity.word.detail.WordDetail;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record WordResponseDto(
        Long wordId,
        Long folderId,
        String word,
        String mean,
        String read,
        String memo,
        String soundPath,
        String memorization,
        String type,
        LocalDateTime updateTime,
        LocalDateTime createTime,
        List<WordDetailResponseDto> details
        ) {
        public static WordResponseDto of(Word word) {
                List<WordDetail> wordDetails = word.getWordDetails();
                Sound sound = word.getSound();
                return WordResponseDto.builder()
                        .wordId(word.getWordId())
                        .folderId(word.getWordId())
                        .word(word.getWord())
                        .mean(word.getMean())
                        .read(word.getRead())
                        .memo(word.getMemo())
                        .soundPath(sound.getSoundPath())
                        .memorization(word.getMemorization())
                        .type(word.getLang().name())
                        .createTime(word.getCreateTime())
                        .updateTime(word.getUpdateTime())
                        .details(wordDetails.stream().map(WordDetailResponseDto::of).toList())
                        .build();
        }
}
