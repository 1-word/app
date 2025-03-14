package com.numo.api.domain.wordbook.word.dto;

import com.numo.domain.base.Timestamped;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.folder.Folder;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.api.domain.wordbook.detail.dto.WordDetailResponseDto;
import lombok.Builder;

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
        String updateTime,
        String createTime,
        List<WordDetailResponseDto> details
        ) {
        public static WordResponseDto of(Word word) {
                List<WordDetail> wordDetails = word.getWordDetails();
                Sound sound = word.getSound();
                Folder folder = word.getFolder();
                return WordResponseDto.builder()
                        .wordId(word.getWordId())
                        .folderId(folder.getFolderId())
                        .soundPath(sound.getWord())
                        .word(word.getWord())
                        .mean(word.getMean())
                        .read(word.getRead())
                        .memo(word.getMemo())
                        .memorization(word.getMemorization())
                        .type(word.getLang().name())
                        .createTime(Timestamped.getTimeString(word.getCreateTime()))
                        .updateTime(Timestamped.getTimeString(word.getUpdateTime()))
                        .details(wordDetails.stream().map(WordDetailResponseDto::of).toList())
                        .build();
        }
}
