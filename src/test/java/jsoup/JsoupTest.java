package jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

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
        String word = "Exception";
        String url = "https://dic.daum.net/search.do?q=${word}".replace("${word}", word);

        Document doc = JsoupTest.getJsoupConnection(url).get();

        System.out.println(doc.title());

        String wordXpath = "//*[@id='mArticle']/div[1]/div[2]/div[2]/div/div[1]/strong/a";
        String defXpath = "//*[@id=\"mArticle\"]/div[1]/div[2]/div[2]/div[1]/ul";

        String result = doc.selectXpath(wordXpath).text();
        String definition = doc.selectXpath(defXpath).text();

        Assertions.assertEquals(word.toLowerCase(), result);
    }
}
