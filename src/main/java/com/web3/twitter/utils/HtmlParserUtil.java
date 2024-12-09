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


    /**
     * 将百分比字符串转换为文本进度条。
     *
     * @param percentageStr 百分比字符串，例如 "8%"
     * @return 文本进度条
     */
    public static String convertPercentageToProgressBar(String percentageStr) {
        // 移除字符串中的百分号
        percentageStr = percentageStr.replace("%", "");
        // 将字符串转换为整数
        int percentage = Integer.parseInt(percentageStr);
        // 进度条的长度
        int barLength = 50;
        // 构建进度条
        StringBuilder progressBar = new StringBuilder("[");
        // 计算进度条中完成的块数
        int completeBlocks = (int) ((double) percentage / 100 * barLength);
        // 添加完成的块
        for (int i = 0; i < completeBlocks; i++) {
            progressBar.append("=");
        }
        // 添加未完成的块
        for (int i = completeBlocks; i < barLength; i++) {
            progressBar.append(" ");
        }
        // 关闭进度条
        progressBar.append("]");

        // 添加百分比
        progressBar.append(" ").append(percentageStr).append("%");

        return progressBar.toString();
    }


    /**
     * 填充进度条
     * @param percentageStr
     * @param barLength
     * @return
     */
    public static String createFillProgressBar(String percentageStr, int barLength) {
        int percentage = Integer.parseInt(percentageStr.replace("%", ""));
        StringBuilder progressBar = new StringBuilder();
        int completeLength = (int) ((double) percentage / 100 * barLength);
        for (int i = 0; i < completeLength; i++) {
            progressBar.append("█");
        }
        for (int i = completeLength; i < barLength; i++) {
            progressBar.append("░");
        }
        progressBar.append(" ").append(percentageStr);
        return progressBar.toString();
    }


    /**
     * ASCII艺术进度条
     * @param percentageStr
     * @param barLength
     * @return
     */
    public static String createAsciiProgressBar(String percentageStr, int barLength) {
        int percentage = Integer.parseInt(percentageStr.replace("%", ""));
        StringBuilder progressBar = new StringBuilder();
        int completeLength = (int) ((double) percentage / 100 * barLength);
        for (int i = 0; i < completeLength; i++) {
            progressBar.append("█");
        }
        for (int i = completeLength; i < barLength; i++) {
            progressBar.append(" ");
        }
        progressBar.append(" ").append(percentageStr);
        return progressBar.toString();
    }

}