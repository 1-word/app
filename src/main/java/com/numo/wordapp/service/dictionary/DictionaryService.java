package com.numo.wordapp.service.dictionary;

import com.numo.wordapp.dto.dictionary.DictionaryCrawlingDto;
import com.numo.wordapp.dto.dictionary.DictionaryDto;
import com.numo.wordapp.entity.dictionary.Dictionary;
import com.numo.wordapp.repository.dictionary.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryCacheService dictionaryCacheService;
    private final DictionaryCrawlingService dictionaryCrawlingService;

    private String key = "dict";

    public boolean exists(String key, String text) {
        Double score = dictionaryCacheService.score(key, text);
        return score.isNaN();
    }

    public DictionaryDto searchWord(String searchText) {
        Optional<Dictionary> dictWord = dictionaryRepository.findByWord(searchText);
        // 해당하는 데이터가 없으면
        if (dictWord.isEmpty()) {
            Dictionary crawlingWord = getCrawlingWord(searchText);
            return DictionaryDto.of(dictionaryRepository.save(crawlingWord));
        }

        // 뜻 데이터가 없다면 크롤링해와서 넣어준다.
        Dictionary dictionary = dictWord.get();
        if (dictionary.checkCrawling()) {
            Dictionary crawlingWord = getCrawlingWord(searchText);
            dictionary.updateMean(crawlingWord.getMean());
            return DictionaryDto.of(dictionaryRepository.save(dictionary));
        }

        return DictionaryDto.of(dictionary);
    }

    private Dictionary getCrawlingWord(String searchText) {
        DictionaryCrawlingDto dictionaryCrawlingDto = dictionaryCrawlingService.searchWord(searchText);
        String mean = String.join(",", dictionaryCrawlingDto.definitions());
        Dictionary saveDict = Dictionary.builder()
                .word(searchText)
                .mean(mean)
                .isCrawling("Y")
                .build();
        return saveDict;
    }

    public List<DictionaryDto> searchWordList(String searchText) {
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
        // 사전 데이터베이스에 해당하는 단어가 등록이 되어있으면 저장하지 않는다.
        if (dictionaryRepository.existsByWord(dictionaryDto.word())) {
            return null;
        }

        DictionaryCrawlingDto resultDto = dictionaryCrawlingService.searchWord(dictionaryDto.word());

        // 실제 사전에 등록이 되어있지 않으면 사전 데이터베이스에 저장하지 않는다.
        if (!Objects.equals(resultDto.word().toLowerCase(), dictionaryDto.word().toLowerCase())) {
            return null;
        }

        String mean = String.join(",", resultDto.definitions());

        Dictionary dictionary = dictionaryDto.toEntity(mean, "Y");
        return DictionaryDto.of(dictionaryRepository.save(dictionary));
    }
}
