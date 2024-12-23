package com.numo.api.service.dictionary;

import com.numo.api.dto.dictionary.DictionaryCrawlingDto;
import org.jsoup.nodes.Document;

public interface DictionaryCrawling {
    DictionaryCrawlingDto getSearchWord(Document doc, String text);
    String getSearchUrl(String word);
}
