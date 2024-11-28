package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.word.detail.ReadWordDetailGroupKey;
import com.numo.wordapp.dto.word.detail.ReadWordDetailListResponseDto;
import com.numo.wordapp.dto.word.detail.ReadWordDetailResponseDto;
import com.numo.wordapp.entity.word.Folder;
import com.numo.wordapp.entity.word.Sound;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.entity.word.detail.WordDetail;
import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
        String type,
        String updateTime,
        String createTime,
        List<ReadWordDetailListResponseDto> details
        ) {
        public static ReadWordResponseDto of(Word word) {
                List<WordDetail> wordDetails = word.getWordDetails();
                Sound sound = word.getSound();
                Folder folder = word.getFolder();
                return ReadWordResponseDto.builder()
                        .wordId(word.getWordId())
                        .folderId(folder.getFolderId())
                        .soundPath(sound.getWord())
                        .word(word.getWord())
                        .mean(word.getMean())
                        .read(word.getRead())
                        .memo(word.getMemo())
                        .memorization(word.getMemorization())
                        .type(word.getLang().name())
                        .createTime(word.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .updateTime(word.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .details(getGroupingWordDetails(wordDetails))
                        .build();
        }

        private static List<ReadWordDetailListResponseDto> getGroupingWordDetails(List<WordDetail> wordDetails) {
                List<ReadWordDetailListResponseDto> result = wordDetails.stream()
                        .collect(Collectors.groupingBy(entry -> new ReadWordDetailGroupKey(
                                        entry.getWordGroup().getWordGroupId(),
                                        entry.getWordGroup().getName()
                                ), Collectors.mapping(entry ->
                                        new ReadWordDetailResponseDto(
                                                entry.getWordDetailId(),
                                                entry.getTitle(),
                                                entry.getContent(),
                                                entry.getCreateTime(),
                                                entry.getUpdateTime()
                                        ), Collectors.toList())
                        ))
                        .entrySet().stream()
                        .map(entry -> new ReadWordDetailListResponseDto(entry.getKey(), entry.getValue()))
                        .toList();
                return result;
        }
}
