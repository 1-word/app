package com.numo.api.global.comm.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupUtil {
    public static Connection getJsoupConnection(String url) throws Exception {
        return Jsoup.connect(url);
    }

    public static Document get(String url) throws Exception {
        Connection conn = getJsoupConnection(url);
        Document result = null;
        result = conn.get();
        return result;
    }

    public static Elements select(String url, String query) throws Exception {
        Connection conn = getJsoupConnection(url);
        Elements result = null;
        result = conn.get().select(query);
        return result;
    }

    public static Elements selectXpath(String url, String xpath) throws Exception {
        Connection conn = getJsoupConnection(url);
        Elements result = null;
        result = conn.get().selectXpath(xpath);
        return result;
    }
}
