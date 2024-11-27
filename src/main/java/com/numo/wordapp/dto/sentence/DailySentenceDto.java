package com.numo.wordapp.dto.sentence;

import com.numo.wordapp.entity.sentence.DailySentence;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DailySentenceDto(
   Long dailySentenceId,
   String sentence,
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
