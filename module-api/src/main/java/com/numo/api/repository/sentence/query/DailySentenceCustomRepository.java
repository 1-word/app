package com.numo.api.repository.sentence.query;

import com.numo.api.dto.sentence.DailySentenceDto;
import com.numo.api.dto.sentence.search.DailySentenceParameterDto;
import com.numo.api.dto.sentence.read.ReadDailyWordDto;
import com.numo.api.dto.sentence.wordDailySentence.WordDailySentenceDto;

import java.util.List;

public interface DailySentenceCustomRepository {
    List<DailySentenceDto> findDailySentencesBy(Long userId, DailySentenceParameterDto parameterDto);
    List<ReadDailyWordDto> findDailyWordsBy(List<Long> sentenceIds);
    List<Integer> findDailySentenceDays(Long userId, DailySentenceParameterDto parameterDto);

    List<WordDailySentenceDto> getWordDailySentenceInfo(Long userId, Long wordDailySentenceId);
}
