package com.numo.api.repository.word;

import com.numo.api.dto.folder.FolderInWordCountDto;
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