package com.numo.api.domain.dictionary.service;

import com.numo.api.global.comm.util.JsoupUtil;
import com.numo.api.domain.dictionary.dto.DictionaryCrawlingDto;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class DictionaryCrawlingService {

    public DictionaryCrawlingDto searchWord(String word) {
        DictionaryCrawling site = create("daum");
        String url = site.getSearchUrl(word);
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
            throw new IllegalArgumentException("잘못된 url 입니다." + url);
        }
    }

}
