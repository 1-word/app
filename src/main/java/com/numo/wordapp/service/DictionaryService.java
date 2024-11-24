package com.numo.wordapp.service;

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

    public List<DictionaryDto> searchWord(String searchText) {
        List<Dictionary> words = dictionaryRepository.findByWordWithLimit(searchText, 5);
        List<DictionaryDto> res = words.stream().map(DictionaryDto::of).toList();

        return res;
    }

    public DictionaryDto save(DictionaryDto dictionaryDto) {
        Dictionary dictionary = dictionaryDto.toEntity();
        return DictionaryDto.of(dictionaryRepository.save(dictionary));
    }
}
