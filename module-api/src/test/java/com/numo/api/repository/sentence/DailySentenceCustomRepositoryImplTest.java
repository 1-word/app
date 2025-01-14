package com.numo.api.repository.sentence;

import com.numo.api.domain.dailySentence.dto.DailySentenceDto;
import com.numo.api.domain.dailySentence.dto.read.ReadDailySentenceDto;
import com.numo.api.domain.dailySentence.dto.read.ReadDailyWordDto;
import com.numo.api.global.comm.date.DateRequestDto;
import com.numo.api.domain.dailySentence.repository.DailySentenceRepository;
import com.numo.domain.sentence.DailySentence;
import com.numo.api.domain.dailySentence.DailySentenceService;
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
        DateRequestDto dto = new DateRequestDto(
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
        DateRequestDto dto = new DateRequestDto(
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