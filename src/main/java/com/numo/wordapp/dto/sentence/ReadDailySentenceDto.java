package com.numo.wordapp.dto.sentence;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReadDailySentenceDto(
   Long dailySentenceId,
   String sentence,
   String mean,
   int year,
   int month,
   int week,
   int day,
   List<DailyWordDto> dailyWords,
   LocalDateTime createTime,
   LocalDateTime updateTime
) {
    public static ReadDailySentenceDto of(DailySentenceDto dailySentenceDto, List<DailyWordDto> words) {
        return ReadDailySentenceDto.builder()
                .dailySentenceId(dailySentenceDto.dailySentenceId())
                .sentence(dailySentenceDto.sentence())
                .mean(dailySentenceDto.mean())
                .year(dailySentenceDto.year())
                .month(dailySentenceDto.month())
                .week(dailySentenceDto.week())
                .day(dailySentenceDto.day())
                .dailyWords(words)
                .createTime(dailySentenceDto.createTime())
                .updateTime(dailySentenceDto.updateTime())
                .build();
    }

}
