package com.numo.wordapp.repository.word;

import com.numo.wordapp.dto.word.DailyWordDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WordRepositoryImplTest {

    @Autowired
    WordRepository wordRepository;

    @Test
    void findByWords() {
        Long userId = 2L;
        List<String> words = List.of("hello", "world", "word", "exception");
        List<DailyWordDto> result = wordRepository.findDailyWordBy(userId, words);
        Assertions.assertEquals("word", result.get(0).word());
    }
}