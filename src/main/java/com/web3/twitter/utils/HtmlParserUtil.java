package com.web3.twitter.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlParserUtil {
    public static String extractOgImage(String html, String attr) {
        // 解析 HTML 文档
        Document document = Jsoup.parse(html);
        // 查找 meta 标签中 property 为 og:image 的内容
        Element ogImageMeta = document.selectFirst(attr);

        // 返回内容，如果没有找到，则返回 null
        return ogImageMeta != null ? ogImageMeta.attr("content") : null;
    }
}