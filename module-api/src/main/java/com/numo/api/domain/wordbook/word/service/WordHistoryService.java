package com.numo.api.domain.wordbook.word.service;

import com.numo.api.domain.wordbook.word.dto.AddWordHistoryDto;
import com.numo.api.domain.wordbook.word.dto.WordHistoryDto;
import com.numo.api.domain.wordbook.word.repository.WordHistoryRepository;
import com.numo.api.domain.wordbook.word.repository.WordRepository;
import com.numo.api.global.comm.util.JsonUtil;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.WordHistory;
import com.numo.domain.wordbook.word.dto.WordSnapShot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordHistoryService {
    private final WordHistoryRepository wordHistoryRepository;
    private final WordRepository wordRepository;

    public List<WordHistoryDto> getWordHistoryByWordBook(Long wordBookId) {
        return null;
    }

    public void saveWordHistory(AddWordHistoryDto historyDto) {
        WordHistory wordHistory = historyDto.toEntity();
        wordHistoryRepository.save(wordHistory);
    }

    public void restore(Long targetWordHistoryId) {
        WordHistory targetWordHistory = wordHistoryRepository.findWordHistoryById(targetWordHistoryId);
        WordHistory restoreHistory = targetWordHistory.createRestoreHistory();
        WordSnapShot wordSnapShot = JsonUtil.makeObject(restoreHistory.getAfterData(), WordSnapShot.class);
        Word word = wordSnapShot.toEntity();
        if (WordHistory.Operation.DELETE == restoreHistory.getOperation()) {
            wordRepository.delete(word);
            return;
        }
        wordRepository.save(word);

    }


}
