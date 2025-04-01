package com.numo.api.domain.dictionary.service;

import com.numo.api.domain.dictionary.dto.DictionaryCrawlingDto;
import com.numo.api.domain.dictionary.dto.DictionaryDto;
import com.numo.api.domain.dictionary.repository.DictionaryRepository;
import com.numo.domain.dictionary.Dictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        DictionaryDto dictionaryDto = DictionaryDto.builder()
                .word(searchText)
                .build();

        DictionaryDto dictionary = save(dictionaryDto);

        return dictionary;
    }

    private Dictionary getCrawlingWord(String searchText) {
        DictionaryCrawlingDto dictionaryCrawlingDto = dictionaryCrawlingService.searchWord(searchText);
        String mean = String.join(",", dictionaryCrawlingDto.definitions());
        Dictionary saveDict = Dictionary.builder()
                .word(dictionaryCrawlingDto.word())
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
        String searchText = dictionaryDto.word();
        Optional<Dictionary> dictWord = dictionaryRepository.findByWord(searchText);
        if (dictWord.isPresent()) {
            Dictionary dictionary = dictWord.get();
            if (dictionary.checkCrawling()) {
                return DictionaryDto.of(dictionary);
            }
            // 데이터는 있으나 크롤링한 내역이 없으면
            Dictionary crawlingWord = getCrawlingWord(searchText);
            dictionary.updateMean(crawlingWord.getMean());
            return DictionaryDto.of(dictionaryRepository.save(dictionary));
        }

        // 크롤링한 적이 없다면
        Dictionary crawlingWord = getCrawlingWord(searchText);
        // 사전에 등록된 데이터가 아니면 데이터를 저장하지 않는다.
        if (!crawlingWord.isRealWord(searchText)) {
            return DictionaryDto.of(crawlingWord);
        }

        return DictionaryDto.of(dictionaryRepository.save(crawlingWord), true);
    }

    public List<DictionaryDto> searchWordListDB(String searchText) {
        return dictionaryRepository.findDictList(searchText);
    }
}
