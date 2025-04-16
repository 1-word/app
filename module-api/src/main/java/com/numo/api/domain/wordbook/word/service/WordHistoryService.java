package com.numo.api.domain.wordbook.word.service;

import com.numo.api.domain.wordbook.word.dto.AddWordHistoryDto;
import com.numo.api.domain.wordbook.word.dto.WordHistoryDto;
import com.numo.api.domain.wordbook.word.repository.WordHistoryRepository;
import com.numo.api.domain.wordbook.word.repository.WordRepository;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.global.comm.util.JsonUtil;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.WordHistory;
import com.numo.domain.wordbook.word.dto.WordSnapShot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void restore(Long userId, Long wordHistoryId) {
        WordHistory targetWordHistory = wordHistoryRepository.findWordHistoryById(wordHistoryId);
        if (targetWordHistory.isRestored()) {
            throw new CustomException(ErrorCode.ALREADY_RESTORED_DATA);
        }
        User user = new User(userId);
        WordHistory newRestoreHistory = targetWordHistory.createRestoreHistory(user);
        WordSnapShot wordSnapShot = JsonUtil.makeObject(newRestoreHistory.getAfterData(), WordSnapShot.class);
        Word word = wordSnapShot.toEntity(newRestoreHistory.getOperation());
        if (WordHistory.Operation.DELETE == newRestoreHistory.getOperation()) {
            wordRepository.delete(word);
            wordHistoryRepository.save(newRestoreHistory);
            return;
        }
        Word savedWord = wordRepository.save(word);
        WordSnapShot newSnapShot = WordSnapShot.copyOf(savedWord);
        String afterData = JsonUtil.toJson(newSnapShot);

        newRestoreHistory.update(savedWord.getWordId(), afterData);
        wordHistoryRepository.save(newRestoreHistory);
    }


}
