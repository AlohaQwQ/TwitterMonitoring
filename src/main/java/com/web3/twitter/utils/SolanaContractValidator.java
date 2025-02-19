package com.web3.twitter.utils;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSON;
import com.web3.twitter.monitorBeans.MonitorCoin;
import com.web3.twitter.redis.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.K;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolanaContractValidator {

    // 修正正则表达式，匹配44位Base58编码的Solana地址
    private static final String SOLANA_ADDRESS_REGEX =
            "(?<![0-9A-HJ-NP-Za-km-z])" + // 前向断言，确保地址独立
                    "[1-9A-HJ-NP-Za-km-z]{44}" +  // 包含小写o的Base58字符集
                    "(?![0-9A-HJ-NP-Za-km-z])";   // 后向断言
    private static final Pattern SOLANA_ADDRESS_PATTERN = Pattern.compile(SOLANA_ADDRESS_REGEX);

    public static boolean isSolanaContract(String address) {
        boolean result = false;
        if (address == null || address.isEmpty()) {
            return result;
        }
        //pump 发射
        if(address.endsWith("pump")){
            result = true;
        } else {
            result = SOLANA_ADDRESS_PATTERN.matcher(address).matches();
        }
        return result;
    }

    public static String matchSolanaContractLineBreak2(String text) {
        String wallet = "";
        if (StringUtils.isEmpty(text)){
            return wallet;
        }

        Matcher matcher = SOLANA_ADDRESS_PATTERN.matcher(text);
        if (matcher.find()) {
            wallet = matcher.group();
        }
        return wallet;
    }

    public static String matchSolanaWalletLineBreak(String text) {
        String wallet = "";
        if (StringUtils.isEmpty(text)) {
            return wallet;
        }

        // 使用更精确的分割逻辑，确保分割后的部分尽可能包含独立地址
        String[] parts = text.split("[\\s,:：.\\n]+");
        for (String part : parts) {
            part = part.toLowerCase();
            // 直接检查每个分割后的部分是否为合法地址
            if (SOLANA_ADDRESS_PATTERN.matcher(part).matches()) {
                wallet = part;
                return wallet; // 找到第一个匹配项后立即返回
            }

            // 处理中英混合内容（例如："合约地址ABC123..." -> ["合约地址", "ABC123..."]）
            String[] subParts = part.split("(?<=\\p{IsHan})(?=\\p{IsLatin})|(?<=\\p{IsLatin})(?=\\p{IsHan})");
            for (String subPart : subParts) {
                subPart = subPart.toLowerCase();
                if (SOLANA_ADDRESS_PATTERN.matcher(subPart).matches()) {
                    wallet = subPart;
                    return wallet; // 找到第一个匹配项后立即返回
                }
            }
        }
        if(StringUtils.isEmpty(wallet)) {
            // 全局二次扫描（处理没有明显分隔符的情况）
            java.util.regex.Matcher globalMatcher = SOLANA_ADDRESS_PATTERN.matcher(text);
            if (globalMatcher.find()) {
                wallet = globalMatcher.group();
            }
        }
        return wallet;
    }

    public static void main(String[] args) {
        String text = "\uD83D\uDC8E 代币信息：\n" +
                "├ 价格：$0\u200B.00003813\n" +
                "├ 市值：$38.13K / SOL池：49.8\n" +
                "├ 流动池：Pump / 5分前\n" +
                "├ 当前进度：84.41%\n" +
                "fxhash上之前的艺术家刚刚发了一个pump已置顶69ErovVFi8iiu2AjG83sNMrW7cgBowmq5mz4NcHmpump快点冲\n" +
                "fxhash上之前的艺术家刚刚发了一个pump已置顶69ErovVFi8iiu2AjG83sNMrW7cgBowmq5mz4NcHmpump快点冲，\n" +
                "已置顶：69ErovVFi8iiu2AjG83sNMrW7cgBowmq5mz4NcHmpump快点冲\n";

        RedisCache redisCache = new RedisCache();
        text = "23szsU1nR58nBMobUYBp7zz3YPdCs6ASaowEhafQe3o5";
        matchSolanaWalletLineBreak(text);
    }

}