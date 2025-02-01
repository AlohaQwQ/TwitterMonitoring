package com.web3.twitter;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.web3.twitter.monitorBeans.MonitorCoin;
import com.web3.twitter.monitorBeans.MonitorUser;
import com.web3.twitter.redis.RedisCache;
import com.web3.twitter.utils.*;
import com.web3.twitter.twitterBeans.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.net.URLEncoder;

@Component
public class TwitterMonitor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TelegramDreamBot telegramDreamBot;

    @Autowired
    private ResourceLoader resourceLoader;

    private final RestTemplate restTemplate;

    /**
     * 用户预置黑名单
     */
    private static JSONArray banArray;

    /**
     * ca预置黑名单
     */
    private static JSONArray caBanArray;

    /**
     * Redis备注列表用户key缓存List
     */
    private static List<String> remarkUserKeyList;

    public TwitterMonitor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        preParseUserBan();
        preParseCaBan();
        //初始化预置解析用户备注列表信息
        preParseUserRemakes();
        //缓存所有备注用户key
        remarkUserKeyList = redisCache.scanAllUserKeys();
        LogUtils.info("初始化用户备注缓存List完成");
        LogUtils.info("初始化完成");
    }

    @Scheduled(fixedRate = 1800) // 每1.5秒执行一次,每天消耗约57600条
    public void scheduleMonitorTask() {
        //https://t.co/FxZZt9AZYf
        //resolveShortLink("https://t.co/FxZZt9AZYf");
        startMonitor();
    }

    @Async("threadPoolTaskExecutor")
    public void startMonitor(){
        String nowTime = DateUtils.getTime();
        LogUtils.info("startMonitor-异步执行: {}", DateUtils.getTimeSSS());
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
        LogUtils.info("startMonitor-聚合api数据返回: {}", DateUtils.getTimeSSS());
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
                                LogUtils.info("startMonitor-Timeline Bean数据解析完成: {}", DateUtils.getTimeSSS());
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
                                                                        long fans;
                                                                        String userName = userResults.getResult().getCore().getScreen_name();
                                                                        //黑名单用户
                                                                        if(banArray!=null && banArray.contains(userName)){
                                                                            LogUtils.info("跳过预置黑名单用户推文", userName);
                                                                            continue;
                                                                        }
                                                                        //测试账号
                                                                        if(userName.contains("daxigua_qwq")){
                                                                            fans = 6000;
                                                                        } else {
                                                                            fans = userResults.getResult().getRelationship_counts().getFollowers();
                                                                        }
                                                                        //粉丝数大于3000
                                                                        if(fans>200){
                                                                            //LogUtils.info("startMonitor-粉丝数大于2000: {}", DateUtils.getTimeSSS());
                                                                            //LogUtils.info("解析推特列表-userResults: {}", userResults);
                                                                            String userID = userResults.getResult().getRest_id();
                                                                            LogUtils.info("解析推特列表-用户restId: {}", userID);
                                                                            if(StringUtils.isEmpty(userID)){
                                                                                LogUtils.error("用户restId-userId为空");
                                                                                continue;
                                                                            }
                                                                            //拼接推特链接 https://x.com/VT_BNB/status/1861334062021185655
                                                                            String userShowName = userResults.getResult().getCore().getName();
                                                                            // 确保使用正确的字符编码
                                                                            String userNameUtf8 = new String(userName.getBytes(), StandardCharsets.UTF_8);

                                                                            //跳过名字包含pump用户
                                                                            if(StringUtils.isNotEmpty(userNameUtf8) &&
                                                                                    (userNameUtf8.contains("Pump") || userNameUtf8.contains("pump"))){
                                                                                LogUtils.info("跳过名字包含pump推文: {}", userNameUtf8);
                                                                                continue;
                                                                            }

                                                                            String tweetUrl = String.format("https://x.com/%s/status/%s", userName, restId);
                                                                            LogUtils.info("解析推特列表-推特链接", tweetUrl);
                                                                            //认证状态
                                                                            boolean verified = userResults.getResult().getVerification().getIs_blue_verified();
                                                                            boolean isGreaterThanTwoDays;
                                                                            MonitorUser user;
                                                                            //LogUtils.info("startMonitor-更新用户信息和共同关注列表: {}", DateUtils.getTimeSSS());
                                                                            //存储用户信息
                                                                            if (!redisCache.hasKey(userNameUtf8)) {
                                                                                user = new MonitorUser();
                                                                                user.setCreateTime(String.valueOf(System.currentTimeMillis()));
                                                                                user.setUserID(userID);
                                                                                user.setUserName(userNameUtf8);
                                                                                // 异步更新用户共同关注列表
                                                                                updateUserFollowersYouKnow(user).thenAccept(monitorUser -> {
                                                                                    if(monitorUser == null || monitorUser.getFollowersYouKnowRemarkSet() == null) {
                                                                                        LogUtils.error("更新用户共同关注列表异常: {}", tweetUrl);
                                                                                    } else {
                                                                                        //更新用户信息
                                                                                        updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                                    }
                                                                                }).exceptionally(ex -> {
                                                                                    LogUtils.error("更新用户共同关注列表失败: {}", tweetUrl, ex);
                                                                                    //updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                                    return null; // 可处理异常或返回默认值
                                                                                });
                                                                                //updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                            } else {
                                                                                String userString = redisCache.getCacheObject(userNameUtf8);
                                                                                user = JSON.parseObject(userString, MonitorUser.class);
                                                                                //黑名单用户跳过
                                                                                if(!StringUtils.isEmpty(user.getIsBan()) && user.getIsBan().equals("1")){
                                                                                    LogUtils.info("跳过黑名单用户推文: {}", userName);
                                                                                    continue;
                                                                                }

                                                                                //跳过pump发射次数大于3次用户
                                                                                if(user.getPumpHistorySet()!=null && !user.getPumpHistorySet().isEmpty()){
                                                                                    if(user.getPumpHistorySet().size()>2){
                                                                                        LogUtils.info("跳过pump发射次数大于3次用户: {}", userName);
                                                                                        continue;
                                                                                    }
                                                                                }

                                                                                if(StringUtils.isEmpty(user.getUserID())){
                                                                                    user.setUserID(userID);
                                                                                }
                                                                                long currentTimeStamp = System.currentTimeMillis();
                                                                                if(StringUtils.isNotEmpty(user.getUpdateTime())){
                                                                                    long updateTimeStamp = Long.parseLong(user.getUpdateTime());
                                                                                    long difference = currentTimeStamp - updateTimeStamp;
                                                                                    isGreaterThanTwoDays = difference > 2 * 24 * 60 * 60 * 1000;
                                                                                    //大于2天才需要更新用户信息和共同关注列表
                                                                                    //共同关注命中备注列表为空时更新
                                                                                    if (user.getFollowersYouKnowRemarkSet()==null || isGreaterThanTwoDays) {
                                                                                        // 异步更新用户共同关注列表
                                                                                        updateUserFollowersYouKnow(user).thenAccept(monitorUser -> {
                                                                                            if(monitorUser == null || monitorUser.getFollowersYouKnowRemarkSet() == null) {
                                                                                                LogUtils.error("更新用户共同关注列表异常: {}", tweetUrl);
                                                                                            } else {
                                                                                                //更新用户信息
                                                                                                updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                                            }
                                                                                        }).exceptionally(ex -> {
                                                                                            LogUtils.error("更新用户共同关注列表失败: {}", tweetUrl, ex);
                                                                                            //updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                                            return null; // 可处理异常或返回默认值
                                                                                        });
                                                                                    }
                                                                                }
                                                                            }

                                                                            //跳过共同关注小于2次用户
                                                                            if(user.getFollowersYouKnowRemarkSet()!=null && user.getFollowersYouKnowRemarkSet().size()<2){
                                                                                LogUtils.info("跳过共同关注小于2次用户: {}", userName);
                                                                                continue;
                                                                            }

                                                                            //LogUtils.info("startMonitor-更新用户信息和共同关注列表完成: {}", DateUtils.getTimeSSS());
                                                                            //解析合约，https://pump.fun/coin/ftGk9Ykt4tXRGRkpRgAbULSUzrj4idzd3PcBCNopump
                                                                            //短链接 https://t.co/ah6z7Rf7qv
                                                                            Legacy legacy = tweetTwitterResultResult.getLegacy();
                                                                            String createdDate = legacy.getCreated_at();
                                                                            //LogUtils.info("解析推特列表-createdDate: {}", createdDate);
                                                                            fullText[0] = legacy.getFull_text();

                                                                            //判断推特对象urls中是否包含pump 链接
                                                                            boolean hasPump = false;
                                                                            Entities entities = legacy.getEntities();
                                                                            if(entities!=null){
                                                                                if(entities.getUrls()!=null && !entities.getUrls().isEmpty()){
                                                                                    for (Urls entitiesUrl : entities.getUrls()) {
                                                                                        String match = "pump.fun/coin";
                                                                                        if(entitiesUrl.getExpanded_url().contains(match)){
                                                                                            fullText[0] = entitiesUrl.getExpanded_url();
                                                                                            LogUtils.info("legacy url包含ca url: {}", fullText[0]);
                                                                                            hasPump = true;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(!hasPump){
                                                                                //全文匹配ca
                                                                                MonitorCoin matchCoin = SolanaContractValidator.matchSolanaContractLineBreak(fullText[0]);
                                                                                if(matchCoin!=null && StringUtils.isNotEmpty(matchCoin.getCoinCa())){
                                                                                    fullText[0] = "https://pump.fun/coin/" + matchCoin.getCoinCa();
                                                                                    LogUtils.info("全文匹配ca成功: {}", fullText[0]);
                                                                                    hasPump = true;
                                                                                }
                                                                            }

                                                                            if(hasPump){
                                                                                //LogUtils.info("startMonitor-推文数据中包含ca链接: {}", DateUtils.getTimeSSS());
                                                                                int parseResult = parsingTweets(user, tweetUrl, createdDate, nowTime, fullText);
                                                                                if(parseResult<0){
                                                                                    LogUtils.error("legacy url包含ca发送推文异常: {}", legacy.getFull_text());
                                                                                }
                                                                            } else {
                                                                                LogUtils.info("startMonitor-推文数据中不包含ca链接: {}", DateUtils.getTimeSSS());
                                                                                //fullText[0] = "Merci TK!\n" +
                                                                                //        "Je penses a un gros sell the news en janvier vis à vis de trump qu'en penses-tu? https://pump.fun/coin/HhNbVY35YVRBeUfHXbSxgKzK87pPQErpsGqsXuuZpump8934jkz";
                                                                                //推文解析
                                                                                if(fullText[0].contains("https://t.co/")){
                                                                                    // 正则表达式查找短链接
                                                                                    Pattern pattern = Pattern.compile("https://t\\.co/[\\w]+");
                                                                                    Matcher matcher = pattern.matcher(fullText[0]);

                                                                                    while (matcher.find()) {
                                                                                        String shortLink = matcher.group();
                                                                                        LogUtils.info("发现短链接: {}", shortLink + " | "+ DateUtils.getTimeSSS());
                                                                                        if(!StringUtils.isEmpty(shortLink)){
                                                                                            // 使用 ShortLinkResolver 解析短链接
                                                                                            resolveShortLink(shortLink).thenAccept(originalLink -> {
                                                                                                if(StringUtils.isEmpty(originalLink)) {
                                                                                                    LogUtils.error("解析短链接异常: {}", DateUtils.getTimeSSS() + " | "+ legacy.getFull_text());
                                                                                                } else {
                                                                                                    fullText[0] = originalLink;
                                                                                                    int parseResult = parsingTweets(user, tweetUrl, createdDate, nowTime, fullText);
                                                                                                    if(parseResult<0){
                                                                                                        LogUtils.error("短链接发送推文异常: {}", DateUtils.getTimeSSS() + " | "+ legacy.getFull_text());
                                                                                                    }
                                                                                                }
                                                                                            }).exceptionally(ex -> {
                                                                                                LogUtils.error("解析短链接失败: {}", legacy.getFull_text(), ex);
                                                                                                return null; // 可处理异常或返回默认值
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                } else if(fullText[0].contains("https://pump.fun/coin")){
                                                                                    LogUtils.info("startMonitor-推文内容中包含pump完整长链接: {}", DateUtils.getTimeSSS());
                                                                                    int parseResult = parsingTweets(user, tweetUrl, createdDate, nowTime, fullText);
                                                                                    if(parseResult<0){
                                                                                        LogUtils.error("长链接发送推文异常: {}", DateUtils.getTimeSSS() + " | " + legacy.getFull_text());
                                                                                        //continue;
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

                }

            } catch (Exception e) {
                LogUtils.error("解析推特列表数据异常-请求地址: {}", url, e);
            }
        }
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> resolveShortLink(String shortUrl) {
        LogUtils.info("resolveShortLink-异步执行: {}", shortUrl + " | "+ DateUtils.getTimeSSS());
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(shortUrl, HttpMethod.GET, null, String.class);
            LogUtils.info("resolveShortLink-短链数据返回: {}", DateUtils.getTimeSSS());
            if (response != null){
                String responseBody = response.getBody();
                // <meta name="twitter:title" content="Bitconnect (BCC) - Pump"/>
                // <meta property="og:image" content="https://pump.fun/coin/4vrWMCgiMHS8Md1sckFPRQdhvviqf8PBVTX1hQ1Npump/opengraph-image-1aq19n?a797fa226007c33d"/>
                // 使用 HtmlParserUtil 解析 og:image
                // [{"redirect": "/coin/EhshGdvVLtCDK3HGEaurbL8S9MBtgmK4qLdYSWTKpump", "status": "308"}
                if(!StringUtils.isEmpty(responseBody)){
                    String coinName = HtmlParserUtil.extractOgImage(responseBody, "meta[name=twitter:title]");
                    if(StringUtils.isEmpty(coinName)){
                        coinName = HtmlParserUtil.extractOgImage(responseBody, "meta[property=og:title]");
                    }
                    String ogImageUrl = HtmlParserUtil.extractOgImage(responseBody, "meta[name=twitter:image]");
                    if(StringUtils.isEmpty(ogImageUrl)){
                        ogImageUrl = HtmlParserUtil.extractOgImage(responseBody, "meta[property=og:image]");
                    }
                    if(responseBody.contains("redirect") && responseBody.contains("coin/")){
                        JSONObject redirectObject = JSONObject.parseObject(responseBody);
                        if(redirectObject.containsKey("redirect")){
                            ogImageUrl = "https://pump.fun" + redirectObject.getString("redirect");
                        }
                    }
                    LogUtils.info("解析后的链接: {}", DateUtils.getTimeSSS() + " | " + coinName + " | "+ ogImageUrl);
                    LogUtils.info("responseBody: {}", responseBody);
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
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 异步获取共同关注者列表，需要authtoken。 获取共同关注者并缓存3天
     * @param monitorUser
     * @return
     */
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<MonitorUser> updateUserFollowersYouKnow(MonitorUser monitorUser) {
        LogUtils.info("updateUserFollowersYouKnow-异步执行: {}", DateUtils.getTimeSSS());
        Response response;
        String responseString = "";
        String url = null;
        int retryCount = 0;
        //monitorUser.setUserID("829482275318484993");
        if(!redisCache.hasKey("TwitterToken")){
            LogUtils.error("获取推特共同关注者接口异常-推特token不存在");
            return CompletableFuture.completedFuture(null);
        }
        String twitterToken = redisCache.getCacheObject("TwitterToken");
        //twitterToken = "ed7f8aa5c2c0c6e8dad045db601955924c5a2d72";
        if(StringUtils.isEmpty(twitterToken)){
            LogUtils.error("获取推特共同关注者接口异常-推特token为空");
            return CompletableFuture.completedFuture(null);
        }
        if(StringUtils.isEmpty(monitorUser.getUserID())){
            LogUtils.error("获取推特共同关注者接口异常-userId为空");
            return CompletableFuture.completedFuture(null);
        }
        String variables = "{\"count\": 1000, \"userId\": \"" + monitorUser.getUserID() + "\",\"includePromotedContent\": false}";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        // 重试次数超过 4 次，返回空的 MonitorUser
        while (retryCount < 4) {
            try {
                // 构建请求
                Request request = new Request.Builder()
                        .url("https://api.apidance.pro/graphql/FollowersYouKnow?variables=" + variables)
                        .addHeader("AuthToken", twitterToken) // 填入实际的 AuthToken
                        .addHeader("apikey", "u8gkbegljnrd8f9bz3ncpn206l68xw") // 填入实际的 API key
                        .get() // Post请求
                        .build();
                response = client.newCall(request).execute();
                LogUtils.info("updateUserFollowersYouKnow-聚合api数据返回: {}", DateUtils.getTimeSSS());
                if (response != null && response.isSuccessful()) {
                    // 解析响应数据
                    responseString = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(responseString);
                    if (jsonObject == null) {
                        LogUtils.error("获取推特共同关注者接口数据返回异常 | response:", responseString);
                        //return CompletableFuture.completedFuture(null);
                        //跳出循环
                        retryCount = 4;
                    } else {
                        // 获取应用列表
                        JSONObject data = jsonObject.getJSONObject("data");
                        if(data!=null) {
                            JSONObject userJsonObject = data.getJSONObject("user");
                            if (userJsonObject != null) {
                                JSONObject timelineJsonObject = userJsonObject.getJSONObject("result").getJSONObject("timeline");
                                if (timelineJsonObject.containsKey("timeline")) {
                                    //解析推特列表对象
                                    JSONObject timeline2JsonObject = timelineJsonObject.getJSONObject("timeline");
                                    if (timeline2JsonObject.containsKey("instructions")) {
                                        JSONArray instructionsArray = timeline2JsonObject.getJSONArray("instructions");
                                        FollowerEntries followerEntries = null;
                                        for (int i = 0; i < instructionsArray.size(); i++) {
                                            JSONObject arrayJSONObject = instructionsArray.getJSONObject(i);
                                            if(arrayJSONObject.containsKey("entries")){
                                                followerEntries = JSON.parseObject(arrayJSONObject.toJSONString(), FollowerEntries.class);
                                                break;
                                            }
                                        }
                                        if(followerEntries == null || followerEntries.getEntries() == null || followerEntries.getEntries().isEmpty()){
                                            LogUtils.info("获取推特共同关注者接口 FollowerEntries数据为空 | response:", responseString);
                                            monitorUser.setFollowersYouKnowRemarkSet(new HashSet<>());
                                            //return CompletableFuture.completedFuture(monitorUser);
                                            //跳出循环
                                            retryCount = 4;
                                        } else {
                                            //LogUtils.info("updateUserFollowersYouKnow-FollowerEntries Bean数据解析完成: {}", DateUtils.getTimeSSS());
                                            //提取列表中所有用户名
                                            List<String> followersYouKnowList = new ArrayList<>();
                                            for (FollowerInfo followerInfo : followerEntries.getEntries()) {
                                                FollowerItemContent followerItemContent = followerInfo.getContent().getItemContent();
                                                if(followerItemContent==null) {
                                                    //LogUtils.error("获取推特共同关注者接口 解析itemContent为空 | followerEntries:", followerEntries.toString());
                                                    LogUtils.info("获取推特共同关注者接口 解析itemContent为空");
                                                    continue;
                                                }
                                                FollowerLegacy followerLegacy = followerItemContent.getUser_results().getResult().getLegacy();
                                                if(followerLegacy!=null){
                                                    if(StringUtils.isNotEmpty(followerLegacy.getScreen_name())) {
                                                        followersYouKnowList.add(followerLegacy.getScreen_name());
                                                    }
                                                }
                                            }
                                            if(followersYouKnowList.isEmpty()){
                                                LogUtils.info("获取推特共同关注者接口 提取共同关注列表数据为空 | response:", responseString);
                                                monitorUser.setFollowersYouKnowRemarkSet(new HashSet<>());
                                                //跳出循环
                                                retryCount = 4;
                                                return CompletableFuture.completedFuture(monitorUser);
                                            } else {
                                                //LogUtils.info("updateUserFollowersYouKnow-获取推特共同关注数据解析完成: {}", DateUtils.getTimeSSS());
                                                //monitorUser.setFollowersYouKnowList(followersYouKnowList);
                                                Set<String> remarkUserKeySet = new HashSet<>(remarkUserKeyList); // 将 List 转换为 HashSet

                                                //取共同关注者和备注列表的交集
                                                Set<String> matchedSet = followersYouKnowList.parallelStream()
                                                        .filter(remarkUserKeySet::contains)
                                                        .collect(Collectors.toSet());
                                                monitorUser.setFollowersYouKnowRemarkSet(matchedSet);
                                                LogUtils.info("updateUserFollowersYouKnow-推特共同关注取交集完成: {}", DateUtils.getTimeSSS());
                                                //跳出循环
                                                retryCount = 4;
                                                if(monitorUser.getFollowersYouKnowRemarkSet().size()<2){
                                                    LogUtils.info("updateUserFollowersYouKnow-推特共同关注列表数小于2: {}", DateUtils.getTimeSSS());
                                                    return CompletableFuture.completedFuture(monitorUser);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            LogUtils.error("获取推特共同关注者接口数据返回异常: {}", jsonObject.toString());
                            retryCount++; // 继续重试
                            if (StringUtils.isNotEmpty(responseString)){
                                LogUtils.error("response: {}", responseString);
                            }
                            responseString = ""; // 清空 responseString，以便重试
                            //if(retryCount>3){
                            //    LogUtils.error("获取推特共同关注者接口数据超过重试上限返回: {}", jsonObject.toString());
                            //    return CompletableFuture.completedFuture(monitorUser);
                            //}
                        }
                    }
                }
            } catch (Exception e){
                LogUtils.error("获取推特共同关注者接口异常-请求地址: {}", variables, e);
                retryCount++; // 继续重试
                if (StringUtils.isNotEmpty(responseString)){
                    LogUtils.error("response: {}", responseString, e);
                }
                responseString = ""; // 清空 responseString，以便重试
                LogUtils.info("获取推特共同关注者接口异常-重试次数:", retryCount);
                //if(retryCount>3){
                //   LogUtils.error("获取推特共同关注者接口数据超过重试上限返回: {}", variables, e);
                //    return CompletableFuture.completedFuture(monitorUser);
                //}
            }
        }

        /*retryCount = 0;
        LogUtils.info("pumpscam-总数-异步执行: {}", DateUtils.getTimeSSS());
        // 重试次数超过 4 次，返回空的 MonitorUser
        while (retryCount < 1) {
            try {
                variables = "q=https://x.com/" + monitorUser.getUserName() + "&only_deleted=false&apikey=utmgqrtgbmw48hz03598ct9vhz4fxb";
                // 构建请求
                Request request = new Request.Builder()
                        .url("https://pumpscam.com/api/v1/scam/search?" + variables)
                        .get() // Post请求
                        .build();
                client = new OkHttpClient().newBuilder().build();
                response = client.newCall(request).execute();
                LogUtils.info("pumpscam-总数-聚合api数据返回: {}", DateUtils.getTimeSSS());
                if (response != null && response.isSuccessful()) {
                    // 解析响应数据
                    responseString = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(responseString);
                    if (jsonObject == null) {
                        LogUtils.error("获取pumpscam总数-接口数据返回异常 | response:", responseString);
                        //跳出循环
                        retryCount = 4;
                        //return CompletableFuture.completedFuture(null);
                    } else {
                        // 获取应用列表
                        JSONObject data = jsonObject.getJSONObject("data");
                        if(data!=null) {
                            if (data.containsKey("total")) {
                                //pump发推次数
                                Integer total = data.getInteger("total");
                                monitorUser.setNumberPumpLaunch(total);
                                //跳出循环
                                retryCount = 4;
                            }
                        } else {
                            LogUtils.error("获取pumpscam接口总数-数据返回异常: {}", jsonObject.toString());
                            retryCount++; // 继续重试
                            if (StringUtils.isNotEmpty(responseString)){
                                LogUtils.error("response: {}", responseString);
                            }
                            responseString = ""; // 清空 responseString，以便重试
                        }
                    }
                }
            } catch (Exception e){
                LogUtils.error("获取pumpscam接口数据总数-返回异常-请求地址: {}", variables, e);
                retryCount++; // 继续重试
                if (StringUtils.isNotEmpty(responseString)){
                    LogUtils.error("response: {}", responseString, e);
                }
                responseString = ""; // 清空 responseString，以便重试
                LogUtils.info("获取pumpscam接口数据总数-重试次数:", retryCount);
            }
        }

        retryCount = 0;
        LogUtils.info("pumpscam-删推-异步执行: {}", DateUtils.getTimeSSS());
        // 重试次数超过 4 次，返回空的 MonitorUser
        while (retryCount < 1) {
            try {
                variables = "q=https://x.com/" +monitorUser.getUserName() + "&only_deleted=true&apikey=utmgqrtgbmw48hz03598ct9vhz4fxb";
                // 构建请求
                Request request = new Request.Builder()
                        .url("https://pumpscam.com/api/v1/scam/search?" + variables)
                        .get() // Post请求
                        .build();
                client = new OkHttpClient().newBuilder().build();
                response = client.newCall(request).execute();
                LogUtils.info("pumpscam-删推-聚合api数据返回: {}", DateUtils.getTimeSSS());
                if (response != null && response.isSuccessful()) {
                    // 解析响应数据
                    responseString = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(responseString);
                    if (jsonObject == null) {
                        LogUtils.error("获取pumpscam删推-接口数据返回异常 | response:", responseString);
                        //跳出循环
                        retryCount = 4;
                        //return CompletableFuture.completedFuture(null);
                    } else {
                        // 获取应用列表
                        JSONObject data = jsonObject.getJSONObject("data");
                        if(data!=null) {
                            if (data.containsKey("total")) {
                                //删除推特次数
                                Integer total = data.getInteger("total");
                                monitorUser.setNumberTwitterDelete(total);
                                //跳出循环
                                retryCount = 4;
                            }
                        } else {
                            LogUtils.error("获取pumpscam接口删推-数据返回异常: {}", jsonObject.toString());
                            retryCount++; // 继续重试
                            if (StringUtils.isNotEmpty(responseString)){
                                LogUtils.error("response: {}", responseString);
                            }
                            responseString = ""; // 清空 responseString，以便重试
                        }
                    }
                }
            } catch (Exception e){
                LogUtils.error("获取pumpscam接口数据删推-返回异常-请求地址: {}", variables, e);
                retryCount++; // 继续重试
                if (StringUtils.isNotEmpty(responseString)){
                    LogUtils.error("response: {}", responseString, e);
                }
                responseString = ""; // 清空 responseString，以便重试
                LogUtils.info("获取pumpscam接口数据删推-重试次数:", retryCount);
            }
        }*/

        return CompletableFuture.completedFuture(monitorUser);
    }

    /**
     * 获取光光推特数据
     * @param monitorUser
     * @return
     */
    public MonitorUser updateUserPumpscam(MonitorUser monitorUser) {
        //LogUtils.info("updateUserFollowersYouKnow-异步执行: {}", DateUtils.getTimeSSS());
        Response response;
        String responseString = "";
        //String url = null;
        //int retryCount = 0;
        //monitorUser.setUserID("829482275318484993");
//        if(!redisCache.hasKey("TwitterToken")){
//            LogUtils.error("获取推特共同关注者接口异常-推特token不存在");
//            return CompletableFuture.completedFuture(null);
//        }
//        String twitterToken = redisCache.getCacheObject("TwitterToken");
//        //twitterToken = "ed7f8aa5c2c0c6e8dad045db601955924c5a2d72";
//        if(StringUtils.isEmpty(twitterToken)){
//            LogUtils.error("获取推特共同关注者接口异常-推特token为空");
//            return CompletableFuture.completedFuture(null);
//        }
//        if(StringUtils.isEmpty(monitorUser.getUserID())){
//            LogUtils.error("获取推特共同关注者接口异常-userId为空");
//            return CompletableFuture.completedFuture(null);
//        }
        String variables = "";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        LogUtils.info("pumpscam-总数-异步执行: {}", DateUtils.getTimeSSS());
        // 重试次数超过 4 次，返回空的 MonitorUser
            try {
                variables = "q=https://x.com/" + monitorUser.getUserName() + "&only_deleted=false&apikey=utmgqrtgbmw48hz03598ct9vhz4fxb";
                // 构建请求
                Request request = new Request.Builder()
                        .url("https://pumpscam.com/api/v1/scam/search?" + variables)
                        .get() // Post请求
                        .build();
                client = new OkHttpClient().newBuilder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS).build();
                response = client.newCall(request).execute();
                LogUtils.info("pumpscam-总数-聚合api数据返回: {}", DateUtils.getTimeSSS());
                if (response != null && response.isSuccessful()) {
                    // 解析响应数据
                    responseString = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(responseString);
                    if (jsonObject == null) {
                        LogUtils.error("获取pumpscam总数-接口数据返回异常 | response:", responseString);
                        //跳出循环
                        //retryCount = 4;
                        //return CompletableFuture.completedFuture(null);
                    } else {
                        if(jsonObject.containsKey("data")){
                            // 获取应用列表
                            JSONObject data = jsonObject.getJSONObject("data");
                            if(data!=null) {
                                if (data.containsKey("total")) {
                                    //pump发推次数
                                    Integer total = data.getInteger("total");
                                    monitorUser.setNumberPumpLaunch(total);
                                    //跳出循环
                                    //retryCount = 4;
                                }
                            } else {
                                LogUtils.error("获取pumpscam接口总数-数据返回异常: {}", jsonObject.toString());
                                //retryCount++; // 继续重试
                                if (StringUtils.isNotEmpty(responseString)){
                                    LogUtils.error("response: {}", responseString);
                                }
                                //responseString = ""; // 清空 responseString，以便重试
                            }
                        } else {
                            LogUtils.info("获取pumpscam接口总数-data为空: {}", jsonObject.toString());
                        }
                    }
                }
            } catch (Exception e){
                LogUtils.error("获取pumpscam接口数据总数-返回异常-请求地址: {}", variables, e);
                if (StringUtils.isNotEmpty(responseString)){
                    LogUtils.error("response: {}", responseString, e);
                }
                //responseString = ""; // 清空 responseString，以便重试
            }

        LogUtils.info("pumpscam-删推-异步执行: {}", DateUtils.getTimeSSS());
        try {
            variables = "q=https://x.com/" +monitorUser.getUserName() + "&only_deleted=true&apikey=utmgqrtgbmw48hz03598ct9vhz4fxb";
            // 构建请求
            Request request = new Request.Builder()
                    .url("https://pumpscam.com/api/v1/scam/search?" + variables)
                    .get() // Post请求
                    .build();
            client = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS).build();
            response = client.newCall(request).execute();
            LogUtils.info("pumpscam-删推-聚合api数据返回: {}", DateUtils.getTimeSSS());
            if (response != null && response.isSuccessful()) {
                // 解析响应数据
                responseString = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseString);
                if (jsonObject == null) {
                    LogUtils.error("获取pumpscam删推-接口数据返回异常 | response:", responseString);
                    //return CompletableFuture.completedFuture(null);
                } else {
                    if(jsonObject.containsKey("data")){
                        // 获取应用列表
                        JSONObject data = jsonObject.getJSONObject("data");
                        if(data!=null) {
                            if (data.containsKey("total")) {
                                //删除推特次数
                                Integer total = data.getInteger("total");
                                monitorUser.setNumberTwitterDelete(total);
                                //跳出循环
                                //retryCount = 4;
                            }
                        } else {
                            LogUtils.error("获取pumpscam接口删推-数据返回异常: {}", jsonObject.toString());
                            //retryCount++; // 继续重试
                            if (StringUtils.isNotEmpty(responseString)){
                                LogUtils.error("response: {}", responseString);
                            }
                            //responseString = ""; // 清空 responseString，以便重试
                        }
                    } else {
                        LogUtils.info("获取pumpscam接口删推-data为空: {}", jsonObject.toString());
                    }
                }
            }
        } catch (Exception e){
            LogUtils.error("获取pumpscam接口数据删推-返回异常-请求地址: {}", variables, e);
            if (StringUtils.isNotEmpty(responseString)){
                LogUtils.error("response: {}", responseString, e);
            }
        }
        return monitorUser;
    }

    /**
     * 推文解析
     * @param fullText
     * @return
     */
    private int parsingTweets(MonitorUser user, String tweetUrl, String createdDate, String nowTime, String[] fullText){
        LogUtils.info("parsingTweets-开始解析推文数据: {}", DateUtils.getTimeSSS(), tweetUrl, fullText);
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

                if(coin.getMentionUserList().size()>10){
                    LogUtils.info("该ca提及次数大于10自动过滤: {} ", pumpCa + " | "+user.getUserName());
                    return result;
                }
                //获取pumpscam数据
                user = updateUserPumpscam(user);

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
                if(StringUtils.isNotEmpty(user.getFansNumber()) && Long.parseLong(user.getFansNumber()) >10000){
                    messageBuilder.append("┌❗粉丝数大于1w");
                    if(Long.parseLong(user.getFansNumber()) >20000){
                        messageBuilder.append("->2w");
                    }
                    if(Long.parseLong(user.getFansNumber()) >50000){
                        messageBuilder.append("->5w");
                    }
                    messageBuilder.append("\n");
                }
                if(StringUtils.isNotEmpty(user.getFansNumber()) &&Long.parseLong(user.getFansNumber()) >100000){
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


                String gmgnUrl = "https://gmgn.ai/sol/token/" + coin.getCoinCa();
                String pumpUrl = "https://pump.fun/coin/" + coin.getCoinCa();
                //https://gmgn.ai/sol/token/3GD2FWYkG2QGXCkN1nEf9TB1jsvt2zvUUEKEmFfgpump
                messageBuilder.append("┌ ca: ").append("<code>").append(coin.getCoinCa()).append("</code>").append("\n");

                //gmgn代币信息
                MonitorCoin monitorCoinInfo = getMonitorCoinInfo(coin);
                LogUtils.info("代币信息：{}",monitorCoinInfo);
                if (StringUtils.isNotEmpty(monitorCoinInfo.getCoinName())) {
                    messageBuilder.append("├ <b>名称: </b> ").append(monitorCoinInfo.getCoinName()).append("\n");
                }
                if (monitorCoinInfo.getCoinPrice()!=null && monitorCoinInfo.getCoinPrice().doubleValue()>0){
                    messageBuilder.append("├ <b>价格: </b> ").append("$").append(monitorCoinInfo.getCoinPrice()).append("\n");
                }
                if (StringUtils.isNotEmpty(monitorCoinInfo.getMarketValue())){
                    messageBuilder.append("├ <b>市值: </b> ").append(monitorCoinInfo.getMarketValue()).append("\n");
                }
                if (StringUtils.isNotEmpty(monitorCoinInfo.getCoinLaunchpad())){
                    messageBuilder.append("├ <b>进度: </b> ").append(monitorCoinInfo.getCoinLaunchpad()).append("\n");
                }
                if (StringUtils.isNotEmpty(monitorCoinInfo.getCreateDate())){
                    messageBuilder.append("├ <b>创建时间: </b> ").append(monitorCoinInfo.getCreateDate()).append("\n");
                }

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

                List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                InlineKeyboardButton twitter = InlineKeyboardButton.builder().text("Twitter").url(tweetUrl).build();
                InlineKeyboardButton gmgn = InlineKeyboardButton.builder().text("Gmgn").url(gmgnUrl).build();
                InlineKeyboardButton pump = InlineKeyboardButton.builder().text("Bloom").url("https://t.me/BloomSolana_bot?start=ref_ShiYi").build();
                inlineKeyboardButtonList.add(twitter);
                inlineKeyboardButtonList.add(gmgn);
                inlineKeyboardButtonList.add(pump);

                InlineKeyboardRow inlineKeyboardRow = new InlineKeyboardRow(inlineKeyboardButtonList);
                List<InlineKeyboardRow> keyboardRows = new ArrayList<>();
                keyboardRows.add(inlineKeyboardRow);

                InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                        .keyboard(keyboardRows)
                        .build();


                if(user.getUserName().contains("daxigua_qwq")){
                    LogUtils.info("parsingTweets-keyboard: {}", keyboard.toString());
                    LogUtils.info("parsingTweets-daxigua_qwq测试发送消息: {}", DateUtils.getTimeSSS());
                    telegramDreamBot.sendText(messageBuilder.toString(), keyboard);
                    return result;
                }
                LogUtils.info("parsingTweets-调用bot发送消息: {}", DateUtils.getTimeSSS());
                //增加dream-bot机器人
                telegramBot.sendText(messageBuilderImport.toString(), messageBuilder.toString());
                telegramDreamBot.sendText(messageBuilder.toString(), keyboard);
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

    /**
     * 获取代币ca信息
     * @param coin
     * @return
     */
    //@Async("threadPoolTaskExecutor")
    public MonitorCoin getMonitorCoinInfo(MonitorCoin coin) {
        LogUtils.info("getMonitorCoinInfo-异步执行: {}", DateUtils.getTimeSSS() + " | " +coin.toString());
        String ca = coin.getCoinCa();
        //"_ga=GA1.1.1489434127.1733035588; __cf_bm=3JqbJ.blZuCCd3J0.zxYe2ZRI4mZAEI9JdyVjgzoRPI-1733670916-1.0.1.1-jA9yDvGTzNN5SWc6t7YUpB9bETko5OE_oC19LKfHSaqkfExGdVe92EinCt1BgkVPh1D1og4AKvnAMOY0xIursg; cf_clearance=632SuyM18YMtOYqHHdfw29Ld2oingqb8Z3NLo9QKkIc-1733670922-1.2.1.1-kdR59Ykh.xBrP9hgeFZC_L85S5AmS8eLqY28Qy.toLYKxnF9OHXVm4fzccsBDuxCCmDrZWpEJSFXqPTJ6uXRuU8ysewNPqBunl0epXNt0WdaSy.K332EHOjplHnGXaqqEGryMKw824NVZKEn4oDsHJ8x9XMI0KkaW5lZSsV91UjuMnCBmbo4GXO_4jxZtTqIC5r47Ruo9v0OKVaPI82DFGkdixtCIbvW12T7YY1bvUNwZ0rvm8VJCiUyEpaLe12selxthFR9ZommeGVlmMZ.iUt69vluSUFlvpb4mEFIQQxL1Rd7JYOWy2bX8iGkcGhLF0qHL3RuzqRmArHodOdx0z0C7sGMzBis.Z.8TLxIG6LfIYIe9ad19NitgmmxZ6CdZ4E1nplMCNE8pYC.dg8FVeK.zmqOuKSBIf95hQ_pP8k; _ga_0XM0LYXGC8=GS1.1.1733670920.13.1.1733670933.0.0.0";
        String cookie = "";
        String responseString = "";
        if (StringUtils.isEmpty(ca)) {
            LogUtils.error("getMonitorCoinInfo-代币ca为空: {}", coin.toString());
            return coin;
        }
        //从redis中获取gmgn-cookie
        if(redisCache.hasKey("GmgnCookie")){
            cookie = redisCache.getCacheObject("GmgnCookie");
        }
        if (StringUtils.isEmpty(cookie)) {
            LogUtils.error("getMonitorCoinInfo-GmgnCookie为空: {}", coin.toString());
            return coin;
        }
        String marketValueUrl = "https://gmgn.ai/defi/quotation/v1/sol/tokens/realtime_token_price?address="+ca+"&decimals="+ca;
        String launchpadUrl = "https://gmgn.ai/api/v1/token_launchpad_info/sol/"+ca;
        String coinNameUrl = "https://gmgn.ai/api/v1/token_info/sol/"+ca;
        String referer = "https://gmgn.ai/sol/token/"+ca;
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        try {
            // 构建请求
            Request request = new Request.Builder()
                    .url(coinNameUrl)
                    .addHeader("Referer", referer) // 填入实际的 AuthToken
                    .addHeader("Cookie", cookie) // 填入实际的 API key
                    .get() // Post请求
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    responseString = response.body().string();
                }
                LogUtils.info("获取代币名称信息成功: {}", responseString);
                org.json.JSONObject jsonObject = new org.json.JSONObject(responseString);
                String name = jsonObject.getJSONObject("data").getString("name");
                String symbol = jsonObject.getJSONObject("data").getString("symbol");
                String circulatingSupply = jsonObject.getJSONObject("data").getString("circulating_supply");
                long creationTimestamp = jsonObject.getJSONObject("data").getLong("creation_timestamp");
                Date date = new Date(creationTimestamp*1000);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);
                coin.setCoinName(symbol + "(" +name + ")");
                coin.setCreateDate(formattedDate);
                coin.setCoinCirculatingSupply(circulatingSupply);
            } else {
                LogUtils.error("获取代币名称信息失败: {}", ca);
            }
        } catch (Exception e) {
            LogUtils.error("获取代币名称信息失败: {}", ca, e);
            if (StringUtils.isNotEmpty(responseString)){
                LogUtils.error("response: {}", responseString, e);
            }
        }

        try {
            // 构建请求
            Request request = new Request.Builder()
                    .url(marketValueUrl)
                    .addHeader("Referer", referer) // 填入实际的 AuthToken
                    .addHeader("Cookie", cookie) // 填入实际的 API key
                    .get() // Post请求
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    responseString = response.body().string();
                }
                LogUtils.info("获取代币市值信息成功: {}", responseString);
                JSONObject jsonObject = JSONObject.parseObject(responseString);
                BigDecimal aDouble = jsonObject.getJSONObject("data").getBigDecimal("usd_price");
                //double convertedYuan = aDouble * 1000000;
                // 设置格式化规则，保留两位小数
                //DecimalFormat df = new DecimalFormat("###0.00");
                //String formattedYuan = df.format(convertedYuan);
                //coin.setMarketValue("$"+formattedYuan+"K");
                coin.setCoinPrice(aDouble);
                //市值=价格*当前流通供给代币量
                if(StringUtils.isNotEmpty(coin.getCoinCirculatingSupply())){
                    BigDecimal supply = new BigDecimal(coin.getCoinCirculatingSupply());
                    double marketValue = NumberUtil.mul(aDouble, supply).doubleValue();
                    String formattedMarketValue;
                    if (marketValue >= 1000000) { // 大于百万
                        formattedMarketValue = String.format("%.2fM", marketValue / 1000000);
                    } else if (marketValue >= 1000) { // 大于千
                        formattedMarketValue = String.format("%.2fK", marketValue / 1000);
                    } else { // 小于千
                        formattedMarketValue = String.valueOf(marketValue);
                    }
                    coin.setMarketValue("$"+formattedMarketValue);
                }
            } else {
                LogUtils.error("获取代币信息失败: {}", ca);
            }
        } catch (Exception e) {
            LogUtils.error("获取代币市值信息失败: {}", ca, e);
            if (StringUtils.isNotEmpty(responseString)){
                LogUtils.error("response: {}", responseString, e);
            }
        }

        try {
            // 构建请求
            Request request = new Request.Builder()
                    .url(launchpadUrl)
                    .addHeader("Referer", referer) // 填入实际的 AuthToken
                    .addHeader("Cookie", cookie) // 填入实际的 API key
                    .get() // Post请求
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    responseString = response.body().string();
                }
                LogUtils.info("获取代币进度信息成功: {}", responseString);
                org.json.JSONObject jsonObject = new org.json.JSONObject(responseString);
                String progress = jsonObject.getJSONObject("data").getString("launchpad_progress");
                String fillProgressBar = "已发射";
                if(!progress.equals("1")){
                    //内盘
                    double value = Double.parseDouble(progress);
                    int percentage = (int) Math.round(value * 100);
                    fillProgressBar = HtmlParserUtil.createFillProgressBar(percentage, 20);
                }
                coin.setCoinLaunchpad(fillProgressBar);
            } else {
                LogUtils.error("获取代币进度信息失败: {}", ca);
            }
        } catch (Exception e) {
            LogUtils.error("获取代币进度信息失败: {}", ca, e);
            if (StringUtils.isNotEmpty(responseString)){
                LogUtils.error("response: {}", responseString, e);
            }
        }
        return coin;
    }

    /**
     * @author Aloha
     * @date 2024/11/30 19:05
     * @description 解析用户黑名单
     */
    public void preParseUserBan() {
        try {
            String filePath = "classpath:static/user-ban.json";
            Resource resource = resourceLoader.getResource(filePath);
            if (!resource.exists()) {
                LogUtils.error("预置解析用户黑名单文件classpath:static/user-ban.json不存在");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();
            banArray = JSON.parseArray(jsonContent.toString());
            //从redis中获取用户黑名单
            if(redisCache.hasKey("UserBanList")){
                String userBanList = redisCache.getCacheObject("UserBanList");
                JSONArray userBanArray = JSON.parseArray(userBanList);
                banArray.addAll(userBanArray);
            }
        } catch (Exception e) {
            LogUtils.error("解析用户黑名单异常", e);
            e.printStackTrace();
        }
        LogUtils.info("初始化解析用户黑名单完成:{}", banArray.toJSONString());
    }

    /**
     * @author Aloha
     * @date 2024/11/30 19:05
     * @description 解析ca黑名单
     */
    public void preParseCaBan() {
        try {
            String filePath = "classpath:static/ca-ban.json";
            Resource resource = resourceLoader.getResource(filePath);
            if (!resource.exists()) {
                LogUtils.error("预置解析ca黑名单文件classpath:static/ca-ban.json不存在");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();
            caBanArray = JSON.parseArray(jsonContent.toString());
            //从redis中获取用户黑名单
            if(redisCache.hasKey("CaBanList")){
                String list = redisCache.getCacheObject("CaBanList");
                JSONArray banArray = JSON.parseArray(list);
                caBanArray.addAll(banArray);
            }
        } catch (Exception e) {
            LogUtils.error("解析ca黑名单异常", e);
            e.printStackTrace();
        }
        LogUtils.info("初始化解析ca黑名单完成:{}", caBanArray.toJSONString());
    }

    /**
     * @author Aloha
     * @date 2024/11/30 19:05
     * @description 解析用户备注列表信息存入redis缓存
     */
    public void preParseUserRemakes() {
        try {
            String filePath = "classpath:static/twtter-remark.json";
            Resource resource = resourceLoader.getResource(filePath);
            if (!resource.exists()) {
                LogUtils.error("预置解析用户备注列表文件classpath:static/twtter-remark.json不存在");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();
            JSONObject jsonObject = JSONObject.parseObject(jsonContent.toString());
            if(jsonObject.containsKey("$myTwitterNoteItems")){
                JSONObject myTwitterNoteItems = jsonObject.getJSONObject("$myTwitterNoteItems");
                for (String key : myTwitterNoteItems.keySet()) {
                    JSONObject userDetails = myTwitterNoteItems.getJSONObject(key);
                    String userNameKey = new String(key.getBytes(), StandardCharsets.UTF_8);

                    MonitorUser user = null;
                    //存储用户信息
                    if(!redisCache.hasKey(userNameKey)){
                        if(userDetails.containsKey("tag")){
                            user = new MonitorUser();
                            //key为用户名称
                            user.setUserName(userNameKey);
                            if(userDetails.containsKey("name")){
                                user.setUserShowName(userDetails.getString("name"));
                            }
                            if(userDetails.containsKey("group")){
                                user.setUserGroup(userDetails.getString("group"));
                            }
                            JSONArray remarksArray = new JSONArray();
                            // 添加备注到 JSONArray
                            remarksArray.add(userDetails.getString("tag"));
                            String remarksString = JSON.toJSONString(remarksArray);
                            user.setUserRemark(remarksString);
                            //设置用户信息时间
                            user.setCreateTime(String.valueOf(System.currentTimeMillis()));
                            user.setUpdateTime(String.valueOf(System.currentTimeMillis()));
                        }
                    } else {
                        if(userDetails.containsKey("tag")){
                            String tag = userDetails.getString("tag");
                            boolean isExists = false;
                            String userString = redisCache.getCacheObject(userNameKey);
                            user = JSON.parseObject(userString, MonitorUser.class);
                            if(!StringUtils.isEmpty(user.getUserRemark())){
                                JSONArray remarksArray = JSON.parseArray(user.getUserRemark());
                                if(remarksArray!=null && !remarksArray.isEmpty()){
                                    for (int i = 0; i < remarksArray.size(); i++) {
                                        String tagExists = remarksArray.getString(i);
                                        if(tagExists.equals(tag)){
                                            isExists = true;
                                            break;
                                        }
                                    }
                                } else {
                                    remarksArray = new JSONArray();
                                }
                                if(!isExists){
                                    //增加新备注
                                    remarksArray.add(tag);
                                    String remarksString = JSON.toJSONString(remarksArray);
                                    user.setUserRemark(remarksString);
                                    user.setUpdateTime(String.valueOf(System.currentTimeMillis()));
                                }
                            }

                        }
                    }
                    if(user!=null){
                        String jsonUser = JSON.toJSONString(user);
                        // 设置缓存对象, userName 为key
                        redisCache.setCacheObject(userNameKey, jsonUser);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.error("解析用户备注列表异常", e);
            e.printStackTrace();
        }
        LogUtils.info("初始化解析用户备注列表完成");
    }

    private void updateUserInfo(MonitorUser user, String userID, String userNameUtf8, String userShowName,
                            long fans, boolean verified) {
        user.setUserID(userID);
        user.setUserName(userNameUtf8);
        user.setUserShowName(userShowName);
        user.setFansNumber(String.valueOf(fans));
        user.setIsCertified(verified ? "已认证" : "未认证");
        user.setUpdateTime(String.valueOf(System.currentTimeMillis()));
        String jsonUser = JSON.toJSONString(user);
        redisCache.setCacheObject(userNameUtf8, jsonUser);
        LogUtils.info("startMonitor-updateUserInfo 更新redis用户信息完成: {}", DateUtils.getTimeSSS());
    }
}