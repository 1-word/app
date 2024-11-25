package com.numo.wordapp.service.dictionary;

import com.numo.wordapp.comm.util.JsoupUtil;
import com.numo.wordapp.dto.dictionary.DaumDictionary;
import com.numo.wordapp.dto.dictionary.DictionaryCrawling;
import com.numo.wordapp.dto.dictionary.DictionaryCrawlingDto;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class DictionaryCrawlingService {
    String dictUrl = "https://dic.daum.net/search.do";
    String q = "q=${word}";

    public DictionaryCrawlingDto searchWord(String word) {
        DictionaryCrawling site = create("daum");
        String url = dictUrl + "?" + getQuery(word);
        Document doc = getDocument(url);
        DictionaryCrawlingDto searchWord = site.getSearchWord(doc, word);
        return searchWord;
    }

    private DictionaryCrawling create(String site) {
        return switch(site){
            case "daum" -> new DaumDictionary();
            default -> throw new IllegalStateException("Unexpected value: " + site);
        };
    }

    private Document getDocument(String url) {
        try {
            Document doc = JsoupUtil.get(url);
            return doc;
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 url 입니다.");
        }
    }

    private String getQuery(String word) {
        return q.replace("${word}", word);
    }
}
