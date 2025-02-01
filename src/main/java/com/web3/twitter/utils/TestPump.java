package com.web3.twitter.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.web3.twitter.monitorBeans.MonitorCoin;
import com.web3.twitter.monitorBeans.MonitorUser;
import com.web3.twitter.redis.RedisCache;
import com.web3.twitter.twitterBeans.Entities;
import com.web3.twitter.twitterBeans.Urls;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TestPump {

    private static JSONArray caBanArray = new JSONArray();
    private static RedisCache redisCache;

    public static void main(String[] args) {
        pump();
    }

    public static void pump(){
        String twitterText = "@ChrisUniverseB @BryceHall what's this?\n" +
                "https://t.co/FIMExAKKFD";
        String nowTime = DateUtils.getTime();
        String createdDate = DateUtils.getTime();
        String tweetUrl = String.format("https://x.com/%s/status/%s", "qq", 1231231);
        boolean hasPump = false;
        //全文匹配ca
        MonitorCoin matchCoin = SolanaContractValidator.matchSolanaContractLineBreak(twitterText);
        if(matchCoin!=null && StringUtils.isNotEmpty(matchCoin.getCoinCa())){
            twitterText = "https://pump.fun/coin/" + matchCoin.getCoinCa();
            hasPump = true;
        }
        MonitorUser user = new MonitorUser();
        if(hasPump){
            //LogUtils.info("startMonitor-推文数据中包含ca链接: {}", DateUtils.getTimeSSS());
            String[] fullText = {twitterText};
            int parseResult = parsingTweets(user, tweetUrl, createdDate, nowTime, fullText);
            if(parseResult<0){
                LogUtils.error("legacy url包含ca发送推文异常: {}", twitterText);
            }
        }
    }

    private static int parsingTweets(MonitorUser user, String tweetUrl, String createdDate, String nowTime, String[] fullText){
        LogUtils.info("parsingTweets-开始解析推文数据: {}", DateUtils.getTimeSSS());
        //pump ca链接
        String pumpCa = "";
        int result = -1;
        //代币
        MonitorCoin coin;
        try {
            if (fullText[0].contains("https://pump.fun/coin")) {
                String coinText = "coin/";
                String pumpText = "pump";
                int startIndex = fullText[0].indexOf(coinText);
                int endIndex = fullText[0].indexOf(pumpText, startIndex);
                if (startIndex != -1 && endIndex != -1) {
                    // 计算裁剪字符串的起始位置
                    startIndex += coinText.length();
                    // 裁剪ca字符串
                    pumpCa = fullText[0].substring(startIndex, endIndex+4);
                } else {
                    LogUtils.error("解析推文-未找到末尾为pump裁剪的字符串: {}", fullText[0]);
                    // 裁剪ca字符串
                    startIndex += coinText.length();
                    pumpCa = fullText[0].substring(startIndex);
                    LogUtils.info("解析推文-裁剪至末尾字符串: {}", pumpCa);
                    //return result;
                }
                if(StringUtils.isEmpty(pumpCa)){
                    LogUtils.error("解析ca异常: {}", fullText[0]);
                    return result;
                }
                if(caBanArray!=null && caBanArray.contains(pumpCa)){
                    LogUtils.error("跳过预置ca黑名单推文", pumpCa);
                    return result;
                }
                //LogUtils.info("parsingTweets-ca解析完成: {}", DateUtils.getTimeSSS());
                //存储ca信息
                if(!redisCache.hasKey(pumpCa)){
                    coin = new MonitorCoin();
                    coin.setCoinCa(pumpCa);
                    List<String> mentionUserList = new ArrayList<>();
                    //添加用户提及ca记录
                    mentionUserList.add(user.getUserID());
                    coin.setMentionUserList(mentionUserList);
                    coin.setCoinName("");
                    coin.setCoinDetail("");
                    coin.setCreateDate("");
                } else {
                    String coinString = redisCache.getCacheObject(pumpCa);
                    coin = JSON.parseObject(coinString, MonitorCoin.class);
                    List<String> mentionUserList = coin.getMentionUserList();
                    if (mentionUserList != null && !mentionUserList.isEmpty()) {
                        for (String mentionUserId : mentionUserList) {
                            //匹配ca提及用户
                            if(StringUtils.isNotEmpty(mentionUserId)
                                    && mentionUserId.equals(user.getUserID())){
                                LogUtils.info("该ca用户已提及: {} ", pumpCa + " | "+user.getUserName());
                                return result;
                            }
                        }
                        //添加用户提及ca记录
                        mentionUserList.add(user.getUserID());
                    }
                }
                //LogUtils.info("parsingTweets-ca信息匹配并存储完成: {}", DateUtils.getTimeSSS());

                String coinJson = JSON.toJSONString(coin);
                redisCache.setCacheObject(pumpCa, coinJson, 3, TimeUnit.DAYS);
                //拼接tg消息发送

                StringBuilder messageBuilder = new StringBuilder(); // 使用 StringBuilder 进行拼接
                StringBuilder messageBuilderImport = new StringBuilder();
                //提及次数大于1

                //命中备注列表
                if(!StringUtils.isEmpty(user.getUserRemark())){
                    messageBuilder.append("⚡\uFE0F 命中备注列表: ").append(user.getUserShowName()).append("\n");
                    messageBuilder.append("\n");
                }

                //历史发射次数统计并记录数据库
                if(user.getPumpHistorySet()==null || user.getPumpHistorySet().isEmpty()){
                    Set<String> pumpSet = new HashSet<>();
                    pumpSet.add(coin.getCoinCa());
                    user.setPumpHistorySet(pumpSet);
                } else {
                    user.getPumpHistorySet().add(coin.getCoinCa());
                    //messageBuilder.append("\uD83D\uDCA5 发射次数: ").append(user.getPumpHistorySet().size()).append("\n");
                    //messageBuilder.append("\n");
                }
                if (user.getNumberPumpLaunch()!=null){
                    messageBuilder.append("\uD83C\uDFAF <b>发盘次数: ").append(user.getNumberPumpLaunch()).append("</b>").append("\n");
                }
                if (user.getNumberTwitterDelete()!=null){
                    messageBuilder.append("\uD83D\uDCA5 <b>删推次数: ").append(user.getNumberTwitterDelete()).append("</b>").append("\n");
                }
//                if (user.getNumberNameChanges()!=null){
//                    messageBuilder.append("├ <b>改名次数: </b> ").append(user.getNumberNameChanges()).append("\n");
//                }
                messageBuilder.append("\n");

                String userNameKey = new String(user.getUserName().getBytes(), StandardCharsets.UTF_8);
                String jsonUser = JSON.toJSONString(user);
                // 设置缓存对象, userName 为key
                redisCache.setCacheObject(userNameKey, jsonUser);

                //粉丝数阶梯提示
                if(Long.parseLong(user.getFansNumber()) >10000){
                    messageBuilder.append("┌❗粉丝数大于1w");
                    if(Long.parseLong(user.getFansNumber()) >20000){
                        messageBuilder.append("->2w");
                    }
                    if(Long.parseLong(user.getFansNumber()) >50000){
                        messageBuilder.append("->5w");
                    }
                    messageBuilder.append("\n");
                }
                if(Long.parseLong(user.getFansNumber()) >100000){
                    messageBuilder.append("└❗粉丝数大于10w");
                    if(Long.parseLong(user.getFansNumber()) >150000){
                        messageBuilder.append("->15w");
                    }
                    if(Long.parseLong(user.getFansNumber()) >200000){
                        messageBuilder.append("->20w");
                    }
                    messageBuilder.append("\n");
                }
                //messageBuilder.append("\n");

                //if(coin.getMentionUserList().size()>1){
                //    messageBuilder.append("\uD83D\uDD25 ca提及次数: ").append(coin.getMentionUserList().size()).append("\n");
                //}

                if(coin.getMentionUserList().size()>10){
                    LogUtils.info("该ca提及次数大于10自动过滤: {} ", pumpCa + " | "+user.getUserName());
                    return result;
                }
                String gmgnUrl = "https://gmgn.ai/sol/token/" + coin.getCoinCa();
                String pumpUrl = "https://pump.fun/coin/" + coin.getCoinCa();
                //https://gmgn.ai/sol/token/3GD2FWYkG2QGXCkN1nEf9TB1jsvt2zvUUEKEmFfgpump
                messageBuilder.append("┌ ca: ").append("<code>").append(coin.getCoinCa()).append("</code>").append("\n");

                //gmgn代币信息
//                MonitorCoin monitorCoinInfo = getMonitorCoinInfo(coin);
//                LogUtils.info("代币信息：{}",monitorCoinInfo);
//                if (StringUtils.isNotEmpty(monitorCoinInfo.getCoinName())) {
//                    messageBuilder.append("├ <b>名称: </b> ").append(monitorCoinInfo.getCoinName()).append("\n");
//                }
//                if (monitorCoinInfo.getCoinPrice()!=null && monitorCoinInfo.getCoinPrice().doubleValue()>0){
//                    messageBuilder.append("├ <b>价格: </b> ").append("$").append(monitorCoinInfo.getCoinPrice()).append("\n");
//                }
//                if (StringUtils.isNotEmpty(monitorCoinInfo.getMarketValue())){
//                    messageBuilder.append("├ <b>市值: </b> ").append(monitorCoinInfo.getMarketValue()).append("\n");
//                }
//                if (StringUtils.isNotEmpty(monitorCoinInfo.getCoinLaunchpad())){
//                    messageBuilder.append("├ <b>进度: </b> ").append(monitorCoinInfo.getCoinLaunchpad()).append("\n");
//                }
//                if (StringUtils.isNotEmpty(monitorCoinInfo.getCreateDate())){
//                    messageBuilder.append("├ <b>创建时间: </b> ").append(monitorCoinInfo.getCreateDate()).append("\n");
//                }

                String gmgnStr = String.format("<a href=\"%s\" >%s</a>", gmgnUrl, "GmGn气泡图");
                String pumpStr = String.format("<a href=\"%s\" >%s</a>", pumpUrl, "PumpK线");
                String twitterStr = String.format("<a href=\"%s\" >%s</a>", tweetUrl, "推文链接");

                // 将<a>标签追加到StringBuilder中
                messageBuilder.append("├ gmgn: ").append(gmgnUrl).append("\n");
                //messageBuilder.append("├ gmgn: ").append(gmgnStr).append("\n");
                messageBuilder.append("├ pump: ").append(pumpUrl).append("\n");
                //messageBuilder.append("└ pump: ").append(pumpStr).append("\n");
                messageBuilder.append("\n");

                messageBuilder.append("┌ <b>twitter: </b>").append(tweetUrl).append("\n");
                messageBuilder.append("├ <b>作者: ").append(user.getUserName())
                        .append(" | ").append(user.getUserShowName()).append("</b>").append("\n");
                messageBuilder.append("├ <b>粉丝数: </b>").append(user.getFansNumber()).append("\n");
                messageBuilder.append("├ <b>是否认证: </b>").append(user.getIsCertified()).append("\n");
                messageBuilder.append("\n");

                //LogUtils.info("parsingTweets-判断用户备注并拼接: {}", DateUtils.getTimeSSS());
                if (StringUtils.isNotEmpty(user.getUserRemark())) {
                    JSONArray remarksArray = JSON.parseArray(user.getUserRemark());
                    messageBuilder.append(remarksArray.stream()
                            .map(remark -> {
//                                String symbol;
//                                int index = remarksArray.indexOf(remark);
//                                if (index == remarksArray.size() - 1) {
//                                    symbol = "└ ";
//                                } else if (index == 0) {
//                                    symbol = "┌ ";
//                                }  else {
//                                    symbol = "├ ";
//                                }
                                return "├ <b>用户备注: " + remark + "</b>";
                            })
                            .collect(Collectors.joining("\n")));
                    if(!remarksArray.isEmpty()){
                        messageBuilder.append("\n");
                    }
                }

                //LogUtils.info("parsingTweets-判断用户共同关注者备注并拼接: {}", DateUtils.getTimeSSS());
                //拼接共同关注者命中备注列表信息
                if (user.getFollowersYouKnowRemarkSet()!=null && !user.getFollowersYouKnowRemarkSet().isEmpty()) {
                    if (user.getFollowersYouKnowRemarkSet().size()>50){
                        messageBuilder.append("├ 关注者备注列表总数: ").append(user.getFollowersYouKnowRemarkSet().size()).append("\n");
                        user.getFollowersYouKnowRemarkSet().parallelStream()
                                .filter(redisCache::hasKey) // 仅保留在缓存中的键
                                .map(redisCache::getCacheObject) // 获取用户字符串
                                .map(Object::toString) // 转换为字符串
                                .map(userString -> JSON.parseObject(userString, MonitorUser.class)) // 转换为 MonitorUser 对象
                                .filter(remarkUser -> !StringUtils.isEmpty(remarkUser.getUserRemark())) // 过滤掉没有备注的用户
                                .flatMap(remarkUser -> {
                                    JSONArray remarksArray = JSON.parseArray(remarkUser.getUserRemark());
                                    return remarksArray.stream()
                                            .map(userRemark -> "├ 关注者备注: " + remarkUser.getUserName() + " | <b>" +userRemark + "</b>\n");
                                })
                                .limit(50) // 只显示前 50 个结果
                                .forEach(messageBuilder::append); // 将结果添加到 messageBuilder
                    } else {
                        user.getFollowersYouKnowRemarkSet().parallelStream()
                                .filter(redisCache::hasKey) // 仅保留在缓存中的键
                                .map(redisCache::getCacheObject) // 获取用户字符串
                                .map(Object::toString) // 转换为字符串
                                .map(userString -> JSON.parseObject(userString, MonitorUser.class)) // 转换为 MonitorUser 对象
                                .filter(remarkUser -> !StringUtils.isEmpty(remarkUser.getUserRemark())) // 过滤掉没有备注的用户
                                .flatMap(remarkUser -> {
                                    JSONArray remarksArray = JSON.parseArray(remarkUser.getUserRemark());
                                    return remarksArray.stream()
                                            .map(userRemark -> "├ 关注者备注: " + remarkUser.getUserName() + " | <b>" +userRemark + "</b>\n");
                                })
                                .forEach(messageBuilder::append); // 将结果添加到 messageBuilder
                    }
                    if(!user.getFollowersYouKnowRemarkSet().isEmpty()){
                        messageBuilder.append("\n");
                    }
                }

                String releaseTime = DateHandleUtil.formatDate(createdDate);
                String pushTime = DateUtils.getTime();
                String newReleaseTime = DateHandleUtil.calculateDifferenceInSeconds(releaseTime, pushTime);
                messageBuilder.append("┌ <b>发布时间: </b>").append(newReleaseTime).append("\n");

                //重点频道，推文用户大于8个共同关注， 并且光光数据库中发盘次数小于2的ca才会被推送到重点频道
                if (!user.getFollowersYouKnowRemarkSet().isEmpty() && user.getFollowersYouKnowRemarkSet().size()>7){
                    if(user.getNumberPumpLaunch()==null || user.getNumberPumpLaunch()<2){
                        messageBuilderImport.append(messageBuilder);
                        messageBuilderImport.append("└ <b>推送时间: </b>").append(DateUtils.getTime()).append("\n");
                        messageBuilder.append("└ <b>推送时间: </b>").append(DateHandleUtil.getTimeAfterOneSecond()).append("\n");
                    } else {
                        //messageBuilder.append("搜索时间: ").append(nowTime).append("\n");
                        messageBuilder.append("└ <b>推送时间: </b>").append(DateUtils.getTime()).append("\n");
                    }
                } else {
                    //messageBuilder.append("搜索时间: ").append(nowTime).append("\n");
                    messageBuilder.append("└ <b>推送时间: </b>").append(DateUtils.getTime()).append("\n");
                }

                /*List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                InlineKeyboardButton twitter = InlineKeyboardButton.builder().text("Twitter").url(tweetUrl).build();
                InlineKeyboardButton gmgn = InlineKeyboardButton.builder().text("Gmgn").url(gmgnUrl).build();
                InlineKeyboardButton pump = InlineKeyboardButton.builder().text("Pump").url(pumpUrl).build();
                inlineKeyboardButtonList.add(twitter);
                inlineKeyboardButtonList.add(gmgn);
                inlineKeyboardButtonList.add(pump);

                InlineKeyboardRow inlineKeyboardRow = new InlineKeyboardRow(inlineKeyboardButtonList);
                List<InlineKeyboardRow> keyboardRows = new ArrayList<>();
                keyboardRows.add(inlineKeyboardRow);

                InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                        .keyboard(keyboardRows)
                        .build();*/

                LogUtils.info("parsingTweets-调用bot发送消息: {}", DateUtils.getTimeSSS());
                //增加dream-bot机器人
                //telegramBot.sendText(messageBuilderImport.toString(), messageBuilder.toString());
                //telegramDreamBot.sendText(messageBuilder.toString());
                result = 1;
            } else {
                LogUtils.error("推文未包含pump代币信息: ", user, tweetUrl, fullText);
            }
        } catch (RuntimeException e){
            LogUtils.error("解析发送推文异常: user:{} tweetUrl:{} fullText:{} exception:{}", user, tweetUrl, fullText, e);
            e.printStackTrace();
        }
        return result;
    }

}
