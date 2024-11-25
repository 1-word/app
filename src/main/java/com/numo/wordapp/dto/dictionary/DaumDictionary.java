package com.numo.wordapp.dto.dictionary;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class DaumDictionary implements DictionaryCrawling {
    private String dictUrl = "https://dic.daum.net/search.do";
    private String q = "q=${word}";

    String wordXpath = "//*[@id='mArticle']/div[1]/div[2]/div[2]/div/div[1]/strong/a";
    String defXpath = "//*[@id=\"mArticle\"]/div[1]/div[2]/div[2]/div[1]/ul";

    @Override
    public DictionaryCrawlingDto getSearchWord(Document doc, String text) {
        String word = doc.selectXpath(wordXpath).text();
        String definition = doc.selectXpath(defXpath).text();
        List<String> definitions = definitionToList(definition);
        return new DictionaryCrawlingDto(word, definition, definitions);
    }

    private List<String> definitionToList(String definition) {
        List<String> result = new ArrayList<>();
        String[] definitions = definition.split("[0-9]+.");
        for (String s: definitions) {
            if (!s.isEmpty()) {
                result.add(s.trim());
            }
        }
        return result;
    }
}
