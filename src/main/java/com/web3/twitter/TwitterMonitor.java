package com.web3.twitter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.web3.twitter.monitorBeans.MonitorCoin;
import com.web3.twitter.monitorBeans.MonitorUser;
import com.web3.twitter.redis.RedisCache;
import com.web3.twitter.utils.DateHandleUtil;
import com.web3.twitter.utils.DateUtils;
import com.web3.twitter.utils.HtmlParserUtil;
import com.web3.twitter.utils.LogUtils;
import com.web3.twitter.twitterBeans.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TwitterMonitor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    private TelegramBot telegramBot;

    private final RestTemplate restTemplate;

    public TwitterMonitor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 3000) // 每1.5秒执行一次,每天消耗约57600条
    public void scheduleMonitorTask() {
        startMonitor();
    }

    @Async("threadPoolTaskExecutor")
    public void startMonitor(){
        String nowTime = DateUtils.getTime();
        LogUtils.info("startMonitor-异步执行: {}", nowTime);
        String url = "https://twitter283.p.rapidapi.com/Search?q=pump.fun&type=Latest&count=20&safe_search=true";
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-rapidapi-host", "twitter283.p.rapidapi.com");
        headers.add("x-rapidapi-key", "c7d7d10b34msh8a76b09b95a1e87p1ff1dcjsn0d20ab44ecb6");
        ResponseEntity<String> response = null;
        // 创建请求实体
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            // 发送 GET 请求
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e){
            LogUtils.error("请求聚合api异常-请求地址: {}", url, e);
        }
        final String[] fullText = {""};
        // 处理响应数据
        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            try {
                // 解析响应数据
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                if (jsonObject == null) {
                    LogUtils.error("推特数据返回异常 | url:", url);
                    return;
                }
                // 获取应用列表
                JSONObject data = jsonObject.getJSONObject("data");
                if(data!=null){
                    JSONObject searchByRawQuery = data.getJSONObject("search_by_raw_query");
                    if(searchByRawQuery!=null){
                        JSONObject searchTimeline = searchByRawQuery.getJSONObject("search_timeline");
                        if (searchTimeline.containsKey("timeline")) {
                            //解析推特列表对象
                            Timeline timeline = JSON.parseObject(searchTimeline.getString("timeline"), Timeline.class);
                            if (timeline != null) {
                                if (timeline.getInstructions() != null && !timeline.getInstructions().isEmpty()) {
                                    List<Instructions> instructionsList = timeline.getInstructions();
                                    for (Instructions instruction : instructionsList) {
                                        List<Entries> entriesList = instruction.getEntries();
                                        if (entriesList != null && !entriesList.isEmpty()) {
                                            for (Entries entries : entriesList) {
                                                Content content = entries.getContent();
                                                if (content != null) {
                                                    ItemContent itemContent = content.getContent();
                                                    if (itemContent != null) {
                                                        Tweet_results tweet_results = itemContent.getTweet_results();
                                                        if (tweet_results != null) {
                                                            TwitterResultResult tweetTwitterResultResult = tweet_results.getResult();
                                                            if (tweetTwitterResultResult != null) {
                                                                String restId = tweetTwitterResultResult.getRest_id();
                                                                //LogUtils.info("解析推特列表-推特restId: {}", restId);
                                                                if(tweetTwitterResultResult.getCore()!=null){
                                                                    //解析用户相关
                                                                    User_results userResults = tweetTwitterResultResult.getCore().getUser_results();
                                                                    if (userResults != null) {
                                                                        //粉丝数
                                                                        long fans = userResults.getResult().getRelationship_counts().getFollowers();
                                                                        //粉丝数大于3000
                                                                        if(fans>3000){
                                                                            //LogUtils.info("解析推特列表-userResults: {}", userResults);
                                                                            String userID = userResults.getResult().getRest_id();
                                                                            //LogUtils.info("解析推特列表-用户restId: {}", userID);
                                                                            //拼接推特链接 https://x.com/VT_BNB/status/1861334062021185655
                                                                            String userName = userResults.getResult().getCore().getScreen_name();
                                                                            String tweetUrl = String.format("https://x.com/%s/status/%s", userName, restId);
                                                                            LogUtils.info("解析推特列表-推特链接: {}", tweetUrl);
                                                                            //认证状态
                                                                            boolean verified = userResults.getResult().getVerification().getIs_blue_verified();
                                                                            MonitorUser user;
                                                                            //存储用户信息
                                                                            if(!redisCache.hasKey(userID)){
                                                                                user = new MonitorUser();
                                                                                user.setUserID(userID);
                                                                                user.setUserName(userName);
                                                                            } else {
                                                                                String userString = redisCache.getCacheObject(userID);
                                                                                user = JSON.parseObject(userString, MonitorUser.class);
                                                                            }
                                                                            user.setFansNumber(String.valueOf(fans));
                                                                            if(verified){
                                                                                user.setIsCertified("已认证");
                                                                            } else {
                                                                                user.setIsCertified("未认证");
                                                                            }
                                                                            String jsonUser = JSON.toJSONString(user);
                                                                            redisCache.setCacheObject(userID, jsonUser, 30, TimeUnit.DAYS);

                                                                            //解析合约，https://pump.fun/coin/ftGk9Ykt4tXRGRkpRgAbULSUzrj4idzd3PcBCNopump
                                                                            //短链接 https://t.co/ah6z7Rf7qv
                                                                            Legacy legacy = tweetTwitterResultResult.getLegacy();
                                                                            String createdDate = legacy.getCreated_at();
                                                                            //LogUtils.info("解析推特列表-createdDate: {}", createdDate);
                                                                            fullText[0] = legacy.getFull_text();

                                                                            //fullText[0] = "Merci TK!\n" +
                                                                            //        "Je penses a un gros sell the news en janvier vis à vis de trump qu'en penses-tu? https://pump.fun/coin/HhNbVY35YVRBeUfHXbSxgKzK87pPQErpsGqsXuuZpump8934jkz";

                                                                            //推文解析
                                                                            if(fullText[0].contains("https://t.co/")){
                                                                                // 正则表达式查找短链接
                                                                                Pattern pattern = Pattern.compile("https://t\\.co/[\\w]+");
                                                                                Matcher matcher = pattern.matcher(fullText[0]);

                                                                                while (matcher.find()) {
                                                                                    String shortLink = matcher.group();
                                                                                    LogUtils.info("发现短链接: {}", shortLink);
                                                                                    if(!StringUtils.isEmpty(shortLink)){
                                                                                        // 使用 ShortLinkResolver 解析短链接
                                                                                        resolveShortLink(shortLink).thenAccept(originalLink -> {
                                                                                            if(StringUtils.isEmpty(originalLink)) {
                                                                                                LogUtils.error("解析短链接异常: {}", legacy.getFull_text());
                                                                                            } else {
                                                                                                fullText[0] = originalLink;
                                                                                                int parseResult = parsingTweets(user, tweetUrl, createdDate, nowTime, fullText);
                                                                                                if(parseResult<0){
                                                                                                    LogUtils.error("短链接发送推文异常: {}", legacy.getFull_text());
                                                                                                }
                                                                                            }
                                                                                        }).exceptionally(ex -> {
                                                                                            LogUtils.error("解析短链接失败: {}", legacy.getFull_text(), ex);
                                                                                            return null; // 可处理异常或返回默认值
                                                                                        });
                                                                                    }
                                                                                }
                                                                            } else if(fullText[0].contains("https://pump.fun/coin")){
                                                                                int parseResult = parsingTweets(user, tweetUrl, createdDate, nowTime, fullText);
                                                                                if(parseResult<0){
                                                                                    LogUtils.error("长链接发送推文异常: {}", legacy.getFull_text());
                                                                                    continue;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }

                }

            } catch (Exception e) {
                LogUtils.error("解析推特列表数据异常-请求地址: {}", url, e);
            }
        }
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> resolveShortLink(String shortUrl) {
        LogUtils.info("resolveShortLink-异步执行: {}", shortUrl);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(shortUrl, HttpMethod.GET, null, String.class);
            if (response != null){
                String responseBody = response.getBody();
                // <meta name="twitter:title" content="Bitconnect (BCC) - Pump"/>
                // <meta property="og:image" content="https://pump.fun/coin/4vrWMCgiMHS8Md1sckFPRQdhvviqf8PBVTX1hQ1Npump/opengraph-image-1aq19n?a797fa226007c33d"/>
                // 使用 HtmlParserUtil 解析 og:image
                if(!StringUtils.isEmpty(responseBody)){
                    String coinName = HtmlParserUtil.extractOgImage(responseBody, "meta[name=twitter:title]");
                    String ogImageUrl = HtmlParserUtil.extractOgImage(responseBody, "meta[property=og:image]");
                    LogUtils.info("解析后的链接: {}", coinName + " | "+ogImageUrl);
                    return CompletableFuture.completedFuture(ogImageUrl);
                }
            } else {
                LogUtils.error("解析短链接失败，未能返回有效的地址: {}", shortUrl);
            }
        } catch (Exception e) {
            LogUtils.error("解析短链接时发生异常: {}", shortUrl, e);
            if (response != null){
                LogUtils.error("response: {}", response.getBody(), e);
            }
        }
        return CompletableFuture.completedFuture("");
    }

    /**
     * 推文解析
     * @param fullText
     * @return
     */
    private int parsingTweets(MonitorUser user, String tweetUrl, String createdDate, String nowTime, String[] fullText){
        //pump ca链接
        String pumpCa = "";
        int result = -1;
        //代币
        MonitorCoin coin;
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
                LogUtils.error("解析推文-未找到裁剪的字符串: {}", fullText[0]);
                return result;
            }
            if(StringUtils.isEmpty(pumpCa)){
                LogUtils.error("解析ca异常: {}", fullText[0]);
                return result;
            }
            //存储ca信息
            if(!redisCache.hasKey(pumpCa)){
                coin = new MonitorCoin();
                coin.setCoinCa(pumpCa);
                coin.setMentionNums("1");
                coin.setCoinName("");
                coin.setCoinDetail("");
                coin.setCreateDate("");
            } else {
                String coinString = redisCache.getCacheObject(pumpCa);
                coin = JSON.parseObject(coinString, MonitorCoin.class);
                String mentionNumsStr = coin.getMentionNums(); // 获取字符串数字
                int mentionNums = Integer.parseInt(mentionNumsStr); // 将字符串转换为整数
                mentionNums += 1; // 累加1
                coin.setMentionNums(String.valueOf(mentionNums)); // 将结果重新设置为字符串
            }
            String coinJson = JSON.toJSONString(coin);
            redisCache.setCacheObject(pumpCa, coinJson, 3, TimeUnit.DAYS);
            //拼接tg消息发送

            StringBuilder messageBuilder = new StringBuilder(); // 使用 StringBuilder 进行拼接
            messageBuilder.append("ca:").append(coin.getCoinCa()).append("\n");
            //messageBuilder.append("ca名称:").append(pumpCa).append("\n");
            //messageBuilder.append("ca创建时间:").append(pumpCa).append("\n");
            messageBuilder.append("ca提及次数:").append(coin.getMentionNums()).append("\n");
            messageBuilder.append("-----------------").append("\n");
            messageBuilder.append("twitter链接:").append(tweetUrl).append("\n");
            messageBuilder.append("作者:").append(user.getUserName()).append("\n");
            messageBuilder.append("粉丝数:").append(user.getFansNumber()).append("\n");
            messageBuilder.append("是否认证").append(user.getIsCertified()).append("\n");
//            messageBuilder.append("备注:").append(pumpCa).append("\n");

            messageBuilder.append("-----------------").append("\n");
            messageBuilder.append("twitter发布时间:").append(DateHandleUtil.convertToDate2(createdDate)).append("\n");
            messageBuilder.append("搜索时间:").append(nowTime).append("\n");
            messageBuilder.append("推送时间:").append(DateUtils.getTime()).append("\n");

            telegramBot.sendText(messageBuilder.toString());
            result = 1;
        }
        return result;
    }
}