package com.numo.wordapp.repository.word;

import com.numo.wordapp.dto.folder.FolderInWordCountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WordCustomRepositoryImplTest {

    @Autowired
    WordRepository wordRepository;

    @Test
    void findFolderInWord() {
        Long userId = 2L;
        List<FolderInWordCountDto> folderInWord = wordRepository.countFolderInWord(userId);
        System.out.println(folderInWord);
    }
}