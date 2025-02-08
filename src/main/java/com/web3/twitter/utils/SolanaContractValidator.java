package com.web3.twitter.utils;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSON;
import com.web3.twitter.monitorBeans.MonitorCoin;
import com.web3.twitter.redis.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.K;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SolanaContractValidator {

    private static final String SOLANA_ADDRESS_REGEX = "^[1-9A-HJ-NP-Za-km-z]{40}pump$";
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

    public static MonitorCoin matchSolanaContractLineBreak(String text) {
        MonitorCoin coin = new MonitorCoin();
        boolean match;
        if (StringUtils.isEmpty(text)) {
            return coin;
        }
        // 按照空格、换行符、逗号分割并过滤空字符串
        String rex = "[\\s,:：.\\n]+";
        // 按照空格、换行符、逗号、中英文冒号分割
        String[] partsOne = text.split(rex, -50);
        for (String partText : partsOne) {
            //外层匹配成功
            //匹配以 "pump" 结尾的文本
            match = SOLANA_ADDRESS_PATTERN.matcher(partText).matches();
            if(match) {
                coin.setCoinCa(partText);
                break;
            }
            //按照 中文英文|英文中文|中文字符|字符中文的方式来切割
            String[] partsTwo = partText.split("(?<=\\p{IsHan})(?=\\p{IsLatin})|(?<=\\p{IsLatin})(?=\\p{IsHan})" +
                    "|(?<=\\p{IsHan})(?=[\\d]+)|(?<=[\\d]+)(?=\\p{IsHan})", -50);
            String[] filteredParts = Arrays.stream(partsTwo).filter(part -> !part.isEmpty()).toArray(String[]::new);
            for (String partMatch : filteredParts) {
                match = SOLANA_ADDRESS_PATTERN.matcher(partMatch).matches();
                if(match) {
                    coin.setCoinCa(partMatch);
                    break;
                }
            }
        }
        return coin;
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
        text = "share about CA : \n" +
                "\n" +
                "BbBqF5fzUSeJDjQJfXq66MFZKA3i7nXc5mRrRuCNpump\n" +
                "\n" +
                "people want a big pump for $simon !";
        matchSolanaContractLineBreak(text);
    }

}