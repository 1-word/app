package com.numo.wordapp.service.dictionary;

import com.numo.wordapp.dto.dictionary.DictionaryCrawlingDto;
import org.jsoup.nodes.Document;

public interface DictionaryCrawling {
    DictionaryCrawlingDto getSearchWord(Document doc, String text);
    String getSearchUrl(String word);
}
