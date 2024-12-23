package com.numo.api.repository.word;

import com.numo.api.dto.sentence.DailyWordListDto;
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
        DailyWordListDto result = wordRepository.findDailyWordBy(userId, words);
        System.out.println(result);
    }
}