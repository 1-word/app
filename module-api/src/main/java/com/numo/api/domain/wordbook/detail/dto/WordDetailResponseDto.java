package com.numo.api.domain.wordbook.detail.dto;

import com.numo.api.domain.wordbook.detail.dto.read.ReadWordDetailGroupKey;
import com.numo.api.domain.wordbook.detail.dto.read.ReadWordDetailListResponseDto;
import com.numo.api.domain.wordbook.detail.dto.read.ReadWordDetailResponseDto;
import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.detail.WordGroup;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record WordDetailResponseDto(
        Long wordId,
        Long wordDetailId,
        Long wordGroupId,
        String groupName,
        String title,
        String content,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
    public static WordDetailResponseDto of(WordDetail wordDetail) {
        WordGroup wordGroup = wordDetail.getWordGroup();
        return WordDetailResponseDto.builder()
                .wordDetailId(wordDetail.getWordDetailId())
                .wordGroupId(wordGroup.getWordGroupId())
                .groupName(wordGroup.getName())
                .title(wordDetail.getTitle())
                .content(wordDetail.getContent())
                .createTime(wordDetail.getCreateTime())
                .updateTime(wordDetail.getUpdateTime())
                .build();
    }

    public static List<ReadWordDetailListResponseDto> grouping(List<WordDetailResponseDto> wordDetails) {
        List<ReadWordDetailListResponseDto> result = wordDetails.stream()
                .collect(Collectors.groupingBy(entry -> new ReadWordDetailGroupKey(
                                entry.wordId(),
                                entry.wordGroupId(),
                                entry.groupName()
                        ), Collectors.mapping(entry ->
                                new ReadWordDetailResponseDto(
                                        entry.wordDetailId(),
                                        entry.title(),
                                        entry.content(),
                                        entry.createTime(),
                                        entry.updateTime()
                                ), Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> new ReadWordDetailListResponseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
        return result;
    }
}
