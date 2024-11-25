package com.numo.wordapp.service.dictionary;

import com.numo.wordapp.dto.dictionary.DictionaryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DictionaryServiceTest {

    @Autowired
    DictionaryService dictionaryService;

    @Test
    void save() {
        String word = "disconnect";
        DictionaryDto dictionaryDto = DictionaryDto.builder()
                .word(word)
                .build();

        DictionaryDto save = dictionaryService.save(dictionaryDto);

        Assertions.assertEquals(word, save.word());
    }
}