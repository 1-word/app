package com.numo.api.domain.dictionary.repository.query;

import com.numo.api.domain.dictionary.dto.DictionaryDto;
import com.numo.domain.dictionary.Dictionary;

import java.util.List;

public interface DictionaryCustomRepository {
    List<Dictionary> findByWordWithLimit(String word, int limit);
    List<DictionaryDto> findDictList(String word);
}
