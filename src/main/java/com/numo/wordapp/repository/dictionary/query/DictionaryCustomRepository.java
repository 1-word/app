package com.numo.wordapp.repository.dictionary.query;

import com.numo.domain.dictionary.Dictionary;

import java.util.List;

public interface DictionaryCustomRepository {
    List<Dictionary> findByWordWithLimit(String word, int limit);

}
