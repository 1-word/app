package com.numo.wordapp.dto.word.detail;

import com.numo.wordapp.entity.word.detail.WordDetail;
import com.numo.wordapp.entity.word.detail.WordGroup;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WordDetailResponseDto(
        Long wordDetailId,
        Long wordGroupId,
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
                .title(wordDetail.getTitle())
                .content(wordDetail.getContent())
                .createTime(wordDetail.getCreateTime())
                .updateTime(wordDetail.getUpdateTime())
                .build();
    }
}
