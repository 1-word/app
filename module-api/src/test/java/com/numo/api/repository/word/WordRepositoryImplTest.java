package com.numo.api.repository.word;

import com.numo.api.domain.dailySentence.dto.DailyWordListDto;
import com.numo.api.domain.wordbook.word.dto.WordDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.domain.wordbook.word.repository.WordRepository;
import com.numo.domain.word.type.SortType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

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

    @Test
    void findWordBy() {
        PageRequest page = PageRequest.of(0, 10);
        Long userId = 2L;
        Long lastWordId = null;
        ReadWordRequestDto req = ReadWordRequestDto.builder()
                .sort(SortType.random)
                .seed(null)
                .build();

        Slice<WordDto> word = wordRepository.findWordBy(page, userId, lastWordId, req);
        System.out.println(word.getContent().get(0));
    }
}