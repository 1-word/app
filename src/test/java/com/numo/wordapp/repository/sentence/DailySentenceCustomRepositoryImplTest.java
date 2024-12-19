package com.numo.wordapp.repository.sentence;

import com.numo.wordapp.dto.sentence.DailySentenceDto;
import com.numo.wordapp.dto.sentence.DailySentenceParameterDto;
import com.numo.wordapp.dto.sentence.ReadDailyWordDto;
import com.numo.wordapp.dto.sentence.ReadDailySentenceDto;
import com.numo.wordapp.entity.sentence.DailySentence;
import com.numo.wordapp.service.sentence.DailySentenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
class DailySentenceCustomRepositoryImplTest {

    @Autowired
    DailySentenceRepository dailySentenceRepository;

    @Autowired
    DailySentenceService dailySentenceService;

    @Test
    void findDailySentencesBy() {
        Long userId = 2L;
        DailySentenceParameterDto dto = new DailySentenceParameterDto(
                null,
                null,
                null,
                null
        );

        List<DailySentenceDto> dailySentencesBy = dailySentenceRepository.findDailySentencesBy(userId, dto);
        System.out.println(dailySentencesBy);
    }

    @Test
    void findDailyWordsBy() {
        List<Long> sentenceIds = List.of(7L, 8L);
        List<ReadDailyWordDto> dailyWords = dailySentenceRepository.findDailyWordsBy(sentenceIds);
        System.out.println(dailyWords);
    }

    @Test
    void getSentenceBy() {
        Long userId = 2L;
        DailySentenceParameterDto dto = new DailySentenceParameterDto(
                null,
                null,
                null,
                null
        );

        List<ReadDailySentenceDto> res = dailySentenceService.getSentenceBy(userId, dto);
        System.out.println(res);
    }

    @Test
//    @Transactional
    @Rollback(value = false)
    void find() {
        Long userId = 2L;
        Long dailySentenceId = 7L;

        DailySentence dailySentence = dailySentenceRepository.findById(dailySentenceId).orElse(null);
        dailySentence.removeDailyWords();
        System.out.println(dailySentence);
    }
}