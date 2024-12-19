package com.numo.wordapp.dto.sentence;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReadDailySentenceDto(
   Long dailySentenceId,
   String sentence,
   String tagSentence,
   String mean,
   int year,
   int month,
   int week,
   int day,
   List<ReadDailyWordDto> dailyWords,
   LocalDateTime createTime,
   LocalDateTime updateTime
) {
    public static ReadDailySentenceDto of(DailySentenceDto dailySentenceDto, List<ReadDailyWordDto> words) {
        return ReadDailySentenceDto.builder()
                .dailySentenceId(dailySentenceDto.dailySentenceId())
                .sentence(dailySentenceDto.sentence())
                .tagSentence(dailySentenceDto.tagSentence())
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
