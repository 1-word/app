package com.numo.api.domain.wordbook.word.service;

import com.numo.api.domain.wordbook.word.repository.WordRepository;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.word.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest
public class WordTestDataCreator {

    @Autowired
    WordRepository wordRepository;

    @DisplayName("테스트 데이터를 생성한다.")
    @Test
    @Rollback(value = false)
    @Transactional
    void createTestData() {
        Long userId = 9L;
        Long soundId = 9L;
        Long wordBookId = 68L;

        Word word = Word.builder()
                .user(new User(userId))
                .sound(new Sound(soundId))
                .wordBook(new WordBook(wordBookId))
                .word("word")
                .read("read")
                .mean("mean")
                .wordDetails(new ArrayList<>())
                .build();

        for (int i=1; i<=10; i++) {
            wordRepository.save(word);
            if (i % 10_000 == 0) {
                System.out.println(i + "번째 데이터 삽입");
            }
        }
    }
}
