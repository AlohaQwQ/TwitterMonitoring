package com.web3.twitter.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BscContractValidator {

    // 修正正则表达式，匹配44位Base58编码的Bsc地址
    private static final String BSC_ADDRESS_REGEX = "^0x[a-fA-F0-9]{40}$";
    private static final Pattern BSC_ADDRESS_PATTERN = Pattern.compile(BSC_ADDRESS_REGEX);

    public static boolean isBscContract(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        return BSC_ADDRESS_PATTERN.matcher(address).matches();
    }

    public static String matchBscWalletLineBreak(String text) {
        String wallet = "";
        if (StringUtils.isEmpty(text)) {
            return wallet;
        }

        // 使用更精确的分割逻辑，确保分割后的部分尽可能包含独立地址
        String[] parts = text.split("[\\s,:：.\\n]+");
        for (String part : parts) {
            part = part.toLowerCase();
            // 直接检查每个分割后的部分是否为合法地址
            if (BSC_ADDRESS_PATTERN.matcher(part).matches()) {
                wallet = part;
                return wallet; // 找到第一个匹配项后立即返回
            }

            // 处理中英混合内容（例如："合约地址ABC123..." -> ["合约地址", "ABC123..."]）
            String[] subParts = part.split("(?<=\\p{IsHan})(?=\\p{IsLatin})|(?<=\\p{IsLatin})(?=\\p{IsHan})");
            for (String subPart : subParts) {
                subPart = subPart.toLowerCase();
                if (BSC_ADDRESS_PATTERN.matcher(subPart).matches()) {
                    wallet = subPart;
                    return wallet; // 找到第一个匹配项后立即返回
                }
            }
        }
        if(StringUtils.isEmpty(wallet)) {
            // 全局二次扫描（处理没有明显分隔符的情况）
            Matcher globalMatcher = BSC_ADDRESS_PATTERN.matcher(text);
            if (globalMatcher.find()) {
                wallet = globalMatcher.group();
            }
        }
        return wallet;
    }

    public static void main(String[] args) {
        String text = "代币地址：\n" +
                "无效地址：0x8afc14fb1954f909c167bdf06c93ed4e1e51592b \n" +
                "正确地址：0x7a250d5630B4cF539739dF2C5dAcb4c659F2488D";

        matchBscWalletLineBreak(text);
    }

}