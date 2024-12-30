package com.numo.api.domain.dictionary.service;

import com.numo.api.domain.dictionary.dto.DictionaryCrawlingDto;
import org.jsoup.nodes.Document;

public interface DictionaryCrawling {
    DictionaryCrawlingDto getSearchWord(Document doc, String text);
    String getSearchUrl(String word);
}
