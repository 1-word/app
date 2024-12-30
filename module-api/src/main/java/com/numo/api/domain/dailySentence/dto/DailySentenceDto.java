package com.numo.api.domain.dailySentence.dto;

import com.numo.domain.sentence.DailySentence;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DailySentenceDto(
   Long dailySentenceId,
   String sentence,
   String tagSentence,
   String mean,
   int year,
   int month,
   int week,
   int day,
   LocalDateTime createTime,
   LocalDateTime updateTime
) {
    public static DailySentenceDto of(DailySentence dailySentence) {
        return DailySentenceDto.builder()
                .dailySentenceId(dailySentence.getDailySentenceId())
                .sentence(dailySentence.getSentence())
                .tagSentence(dailySentence.getTagSentence())
                .mean(dailySentence.getMean())
                .year(dailySentence.getYear())
                .month(dailySentence.getMonth())
                .week(dailySentence.getWeek())
                .day(dailySentence.getDay())
                .createTime(dailySentence.getCreateTime())
                .updateTime(dailySentence.getUpdateTime())
                .build();
    }
}
