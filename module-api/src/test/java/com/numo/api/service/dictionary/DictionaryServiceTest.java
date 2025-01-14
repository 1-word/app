package com.numo.api.service.dictionary;

import com.numo.api.domain.dictionary.dto.DictionaryDto;
import com.numo.api.domain.dictionary.service.DictionaryService;
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