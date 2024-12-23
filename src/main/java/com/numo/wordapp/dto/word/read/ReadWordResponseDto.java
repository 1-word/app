package com.numo.wordapp.dto.word.read;

import com.numo.wordapp.dto.word.WordDto;
import com.numo.wordapp.dto.word.detail.read.ReadWordDetailListResponseDto;
import com.numo.wordapp.entity.Timestamped;
import lombok.Builder;

import java.util.List;

@Builder
public record ReadWordResponseDto(
        Long wordId,
        Long folderId,
        String word,
        String mean,
        String read,
        String memo,
        String soundPath,
        String memorization,
        String updateTime,
        String createTime,
        List<ReadWordDetailListResponseDto> details
        ) {
        public static ReadWordResponseDto of(WordDto word, List<ReadWordDetailListResponseDto> details) {
                return ReadWordResponseDto.builder()
                        .wordId(word.wordId())
                        .folderId(word.folderId())
                        .soundPath(word.soundPath())
                        .word(word.word())
                        .mean(word.mean())
                        .read(word.read())
                        .memo(word.memo())
                        .memorization(word.memorization())
                        .createTime(Timestamped.getTimeString(word.createTime()))
                        .updateTime(Timestamped.getTimeString(word.updateTime()))
                        .details(details)
                        .build();
        }
}
