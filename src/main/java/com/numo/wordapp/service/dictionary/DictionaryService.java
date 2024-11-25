package com.numo.wordapp.service.dictionary;

import com.numo.wordapp.dto.DictionaryDto;
import com.numo.wordapp.entity.dictionary.Dictionary;
import com.numo.wordapp.repository.dictionary.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryCacheService dictionaryCacheService;
    private String key = "dict";

    public List<DictionaryDto> search(String searchText) {
        List<DictionaryDto> res = null;
        int limit = 5;
        List<String> cacheWords = dictionaryCacheService.search(key, searchText, limit);

        if (!cacheWords.isEmpty()) {
            res = cacheWords.stream().map(s -> DictionaryDto.builder().word(s).build()).toList();
            return res;
        }

        List<Dictionary> words = dictionaryRepository.findByWordWithLimit(searchText, limit);

        res = words.stream().map(DictionaryDto::of).toList();

        return res;
    }

    public DictionaryDto save(DictionaryDto dictionaryDto) {
        Dictionary dictionary = dictionaryDto.toEntity();
        return DictionaryDto.of(dictionaryRepository.save(dictionary));
    }
}
