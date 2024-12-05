package com.numo.wordapp.dto.dictionary;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DaumDictionary implements DictionaryCrawling {
    private String dictUrl = "https://dic.daum.net/search.do";
    private String q = "q=${word}";

    String wordXpath = "//*[@id='mArticle']/div[1]/div[2]/div[2]/div/div[1]/strong/a";
    String defXpath = "//*[@id=\"mArticle\"]/div[1]/div[2]/div[2]/div[1]/ul";
    String defXpath2 = "//*[@id=\"mArticle\"]/div[1]/div[1]/div[2]/div/ul";

    @Override
    public DictionaryCrawlingDto getSearchWord(Document doc, String text) {
        String word = doc.selectXpath(wordXpath).text();
        String definition = doc.selectXpath(defXpath).text();
        if (!definitionPatternMatch(definition)) {
            definition = doc.selectXpath(defXpath2).text();
        }

        List<String> definitions = new ArrayList<>();
        if (definition.isEmpty()) {
            definition = null;
        } else {
            definitions = definitionToList(definition);
        }
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

    private boolean definitionPatternMatch(String definition) {
        String patternString = "[0-9]+\\.[\\s가-힣.\\d~!@#$%^&*()_+\\[\\]\\;',/…]+";
        return Pattern.matches(patternString, definition);
    }


}
