package com.numo.api.domain.dailySentence.repository.query;

import com.numo.api.domain.dailySentence.dto.DailySentenceDto;
import com.numo.api.global.comm.date.DateRequestDto;
import com.numo.api.domain.dailySentence.dto.read.ReadDailyWordDto;
import com.numo.api.domain.dailySentence.dto.wordDailySentence.WordDailySentenceDto;

import java.util.List;

public interface DailySentenceCustomRepository {
    List<DailySentenceDto> findDailySentencesBy(Long userId, DateRequestDto parameterDto);
    List<ReadDailyWordDto> findDailyWordsBy(List<Long> sentenceIds);
    List<Integer> findDailySentenceDays(Long userId, DateRequestDto parameterDto);

    List<WordDailySentenceDto> getWordDailySentenceInfo(Long userId, Long wordDailySentenceId);
}
