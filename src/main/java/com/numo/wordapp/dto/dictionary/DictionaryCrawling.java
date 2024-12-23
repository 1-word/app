package com.numo.wordapp.dto.dictionary;

import org.jsoup.nodes.Document;

public interface DictionaryCrawling {
    DictionaryCrawlingDto getSearchWord(Document doc, String text);
    String getSearchUrl(String word);
}
