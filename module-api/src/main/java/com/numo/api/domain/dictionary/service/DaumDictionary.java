package com.numo.api.domain.dictionary.service;

import com.numo.api.domain.dictionary.dto.DictionaryCrawlingDto;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DaumDictionary implements DictionaryCrawling {
    private String dictUrl = "https://dic.daum.net/search.do";
    private String q = "q=${word}";

    String wordXpath = "//*[@id='mArticle']/div[1]/div[2]/div[2]/div/div[1]/strong/a";
    String meanXpath = "//*[@id=\"mArticle\"]/div[1]/div[2]/div[2]/div[1]/ul";
    String meanXpath2 = "//*[@id=\"mArticle\"]/div[1]/div[1]/div[2]/div/ul";

    String patternString = "[0-9]+\\.[\\s가-힣.\\d~!@#$%^&*()_+\\[\\];',/…·]+";

    @Override
    public DictionaryCrawlingDto getSearchWord(Document doc, String text) {
        String word = doc.selectXpath(wordXpath).text();
        String mean = doc.selectXpath(meanXpath).text();
        mean = getMeanPattern(mean);
        if (!meanPatternMatch(mean)) {
            mean = doc.selectXpath(meanXpath2).text();
        }

        List<String> definitions = new ArrayList<>();
        if (mean.isEmpty()) {
            mean = null;
        } else {
            definitions = meanToList(mean);
        }
        return new DictionaryCrawlingDto(word, mean, definitions);
    }

    @Override
    public String getSearchUrl(String word) {
        return dictUrl + "?" + getQuery(word);
    }

    private String getQuery(String word) {
        return q.replace("${word}", word);
    }

    // 뜻이 하나일 때 앞에 숫자가 없어 패턴 매칭 실패하므로 직접 만들어준다.
    private String getMeanPattern(String mean) {
       if (!mean.isEmpty() && !meanPatternMatch(mean))  {
           return "1." + mean;
       }
       return mean;
    }

    private List<String> meanToList(String definition) {
        List<String> result = new ArrayList<>();
        String[] definitions = definition.split("[0-9]+.");
        for (String s: definitions) {
            if (!s.isEmpty()) {
                result.add(s.trim());
            }
        }
        return result;
    }

    private boolean meanPatternMatch(String mean) {
        return Pattern.matches(patternString, mean);
    }


}
