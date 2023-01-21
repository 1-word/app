package com.numo.wordapp.service;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Word;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordService {
    @Transactional
    String updateByWord(WordDto.Request dto);

    @Transactional
    String setByWord(WordDto.Request dto);

    String removeByWord(int id);

    //Word getBySearchWord(int word_id);

    List<Word> getBySearchWord(String user_id, String data);

    List<Word> getByAllWord(String user_id);
}
