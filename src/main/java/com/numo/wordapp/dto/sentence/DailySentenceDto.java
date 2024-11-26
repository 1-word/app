package com.numo.wordapp.dto.sentence;

import com.numo.wordapp.dto.word.DailyWordDto;
import com.numo.wordapp.entity.sentence.DailySentence;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record DailySentenceDto(
   Long dailySentenceId,
   String sentence,
   String mean,
   int year,
   int month,
   int week,
   int day,
   List<DailyWordDto> words,
   LocalDateTime createTime,
   LocalDateTime updateTime
) {
    public static DailySentenceDto of(DailySentence dailySentence, List<DailyWordDto> words) {
        return DailySentenceDto.builder()
                .dailySentenceId(dailySentence.getDailySentenceId())
                .sentence(dailySentence.getSentence())
                .mean(dailySentence.getMean())
                .year(dailySentence.getYear())
                .month(dailySentence.getMonth())
                .week(dailySentence.getWeek())
                .day(dailySentence.getDay())
                .words(words)
                .createTime(dailySentence.getCreateTime())
                .updateTime(dailySentence.getUpdateTime())
                .build();
    }
}
