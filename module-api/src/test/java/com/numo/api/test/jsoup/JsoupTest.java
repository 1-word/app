package com.numo.api.test.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.util.regex.Pattern;

public class JsoupTest {
    public static Connection getJsoupConnection(String url) throws Exception {
        return Jsoup.connect(url);
    }

    public static Elements getJsoupElements(Connection connection, String url, String query) throws Exception {
        Connection conn = !ObjectUtils.isEmpty(connection) ? connection : getJsoupConnection(url);
        Elements result = null;
        result = conn.get().select(query);
        return result;
    }

    @Test
    void start() throws Exception {
        String word = "efface";
        String url = "https://dic.daum.net/search.do?q=${word}".replace("${word}", word);

        Document doc = JsoupTest.getJsoupConnection(url).get();

        System.out.println(doc.title());

        String wordXpath = "//*[@id='mArticle']/div[1]/div[2]/div[2]/div/div[1]/strong/a";
        String defXpath = "//*[@id='mArticle']/div[1]/div[2]/div[2]/div/ul";


        //*[@id="mArticle"]/div[1]/div[2]/div[2]/div/ul
        // 위에꺼가 뜻이 매칭이 안된다면 아래꺼 시도
        //*[@id="mArticle"]/div[1]/div[1]/div[2]/div/ul

        String result = doc.selectXpath(wordXpath).text();
        String definition = doc.selectXpath(defXpath).text();

        System.out.println(result);
        System.out.println(definition);

        Assertions.assertEquals(word.toLowerCase(), result);
    }

    @Test
    void regexTest() {
        String mean = "1.안녕하세요2.안부3.여보세요.";
        String mean2 = "remove completely from recognition or memory";

        String patternString = "[0-9]+\\.[\\s가-힣.\\d~!@#$%^&*()_+\\[\\]\\;',/]+";
        System.out.println(Pattern.matches(patternString, mean));
        System.out.println(Pattern.matches(patternString, mean2));
    }
}
