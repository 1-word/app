package com.numo.wordapp.repository.dictionary;

import com.numo.wordapp.entity.dictionary.Dictionary;

import java.util.List;

public interface DictionaryCustomRepository {
    List<Dictionary> findByWordWithLimit(String word, int limit);

}
