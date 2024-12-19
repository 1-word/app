package com.numo.wordapp.repository.sentence;

import com.numo.wordapp.dto.sentence.DailySentenceDto;
import com.numo.wordapp.dto.sentence.DailySentenceParameterDto;
import com.numo.wordapp.dto.sentence.ReadDailyWordDto;
import com.numo.wordapp.dto.sentence.WordDailySentenceDto;

import java.util.List;

public interface DailySentenceCustomRepository {
    List<DailySentenceDto> findDailySentencesBy(Long userId, DailySentenceParameterDto parameterDto);
    List<ReadDailyWordDto> findDailyWordsBy(List<Long> sentenceIds);
    List<Integer> findDailySentenceDays(Long userId, DailySentenceParameterDto parameterDto);

    List<WordDailySentenceDto> getWordDailySentenceInfo(Long userId, Long wordDailySentenceId);
}
