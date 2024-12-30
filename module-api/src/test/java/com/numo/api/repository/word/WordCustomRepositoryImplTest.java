package com.numo.api.repository.word;

import com.numo.api.domain.wordbook.folder.dto.read.FolderInWordCountDto;
import com.numo.api.domain.wordbook.word.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class WordCustomRepositoryImplTest {

    @Autowired
    WordRepository wordRepository;

    @Test
    void findFolderInWord() {
        Long userId = 2L;
        Map<Long, FolderInWordCountDto> folderInWord = wordRepository.countFolderInWord(userId);
        System.out.println(folderInWord);
    }
}