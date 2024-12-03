package com.web3.twitter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * Redis备注列表用户key缓存List
     */
    private static List<String> remarkUserKeyList;

    public TwitterMonitor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        preParseUserBan();
        //初始化预置解析用户备注列表信息
        preParseUserRemakes();
        //缓存所有备注用户key
        remarkUserKeyList = redisCache.scanAllUserKeys();
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
        //LogUtils.info("startMonitor-异步执行: {}", nowTime);
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
                                                                        long fans;
                                                                        String userName = userResults.getResult().getCore().getScreen_name();
                                                                        //测试账号
                                                                        if(userName.contains("daxigua_qwq")){
                                                                            fans = 6000;
                                                                        } else {
                                                                            fans = userResults.getResult().getRelationship_counts().getFollowers();
                                                                        }
                                                                        //粉丝数大于3000
                                                                        if(fans>3000){
                                                                            //LogUtils.info("解析推特列表-userResults: {}", userResults);
                                                                            String userID = userResults.getResult().getRest_id();
                                                                            //LogUtils.info("解析推特列表-用户restId: {}", userID);
                                                                            //拼接推特链接 https://x.com/VT_BNB/status/1861334062021185655
                                                                            userName = userResults.getResult().getCore().getScreen_name();
                                                                            //黑名单用户
                                                                            if(banArray!=null && banArray.contains(userName)){
                                                                                LogUtils.info("跳过预置黑名单用户推文", userName);
                                                                                continue;
                                                                            }
                                                                            String userShowName = userResults.getResult().getCore().getName();
                                                                            // 确保使用正确的字符编码
                                                                            String userNameUtf8 = new String(userName.getBytes(), StandardCharsets.UTF_8);
                                                                            String tweetUrl = String.format("https://x.com/%s/status/%s", userName, restId);
                                                                            LogUtils.info("解析推特列表-推特链接", tweetUrl);
                                                                            //认证状态
                                                                            boolean verified = userResults.getResult().getVerification().getIs_blue_verified();
                                                                            boolean isGreaterThanTwoDays;
                                                                            MonitorUser user;
                                                                            //存储用户信息
                                                                            if (!redisCache.hasKey(userNameUtf8)) {
                                                                                user = new MonitorUser();
                                                                                user.setCreateTime(String.valueOf(System.currentTimeMillis()));
                                                                                updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                            } else {
                                                                                String userString = redisCache.getCacheObject(userNameUtf8);
                                                                                user = JSON.parseObject(userString, MonitorUser.class);
                                                                                //黑名单用户跳过
                                                                                if(!StringUtils.isEmpty(user.getIsBan()) && user.getIsBan().equals("1")){
                                                                                    LogUtils.info("跳过黑名单用户推文: {}", userName);
                                                                                    continue;
                                                                                }
                                                                                long currentTimeStamp = System.currentTimeMillis();
                                                                                long updateTimeStamp = Long.parseLong(user.getUpdateTime());
                                                                                long difference = currentTimeStamp - updateTimeStamp;
                                                                                isGreaterThanTwoDays = difference > 2 * 24 * 60 * 60 * 1000;
                                                                                //大于2天才需要更新用户信息和共同关注列表
                                                                                //共同关注命中备注列表为空时更新
                                                                                if (user.getFollowersYouKnowRemarkSet()==null ||
                                                                                        user.getFollowersYouKnowRemarkSet().isEmpty() || isGreaterThanTwoDays) {
                                                                                    // 异步更新用户共同关注列表
                                                                                    updateUserFollowersYouKnow(user).thenAccept(monitorUser -> {
                                                                                        if(monitorUser == null) {
                                                                                            LogUtils.error("更新用户共同关注列表异常: {}", tweetUrl);
                                                                                        } else {
                                                                                            //更新用户信息
                                                                                            updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                                        }
                                                                                    }).exceptionally(ex -> {
                                                                                        LogUtils.error("更新用户共同关注列表失败: {}", tweetUrl, ex);
                                                                                        updateUserInfo(user, userID, userNameUtf8, userShowName, fans, verified);
                                                                                        return null; // 可处理异常或返回默认值
                                                                                    });
                                                                                }
                                                                            }

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
                                                                                            hasPump = true;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            if(hasPump){
                                                                                int parseResult = parsingTweets(user, tweetUrl, createdDate, nowTime, fullText);
                                                                                if(parseResult<0){
                                                                                    LogUtils.error("legacy url包含ca发送推文异常: {}", legacy.getFull_text());
                                                                                }
                                                                            } else {
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
                    if(StringUtils.isEmpty(coinName)){
                        coinName = HtmlParserUtil.extractOgImage(responseBody, "meta[property=og:title]");
                    }
                    String ogImageUrl = HtmlParserUtil.extractOgImage(responseBody, "meta[name=twitter:image]");
                    if(StringUtils.isEmpty(ogImageUrl)){
                        ogImageUrl = HtmlParserUtil.extractOgImage(responseBody, "meta[property=og:image]");
                    }
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
     * 异步获取共同关注者列表，需要authtoken。 获取共同关注者并缓存3天
     * @param monitorUser
     * @return
     */
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<MonitorUser> updateUserFollowersYouKnow(MonitorUser monitorUser) {
        ResponseEntity<String> response = null;
        String url = null;
        try {
            String nowTime = DateUtils.getTime();
            LogUtils.info("updateUserFollowersYouKnow-异步执行: {}", nowTime);

            if(!redisCache.hasKey("TwitterToken")){
                LogUtils.error("获取推特共同关注者接口异常-推特token不存在");
                return CompletableFuture.completedFuture(monitorUser);
            }
            String twitterToken = redisCache.getCacheObject("TwitterToken");
            //twitterToken = "ed7f8aa5c2c0c6e8dad045db601955924c5a2d72";
            if(StringUtils.isEmpty(twitterToken)){
                LogUtils.error("获取推特共同关注者接口异常-推特token为空");
                return CompletableFuture.completedFuture(monitorUser);
            }

            String variables = "{\"userId\": \"" + monitorUser.getUserID() + "\", \"count\": 20, \"includePromotedContent\": false}";

            url = UriComponentsBuilder.fromHttpUrl("https://api.apidance.pro/graphql/FollowersYouKnow")
                    .queryParam("variables", variables)
                    .toUriString(); // 不使用 .encode()，避免不必要的编码


            // 创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apikey", "u8gkbegljnrd8f9bz3ncpn206l68xw");
            //headers.add("Host", "api.apidance.pro");
            headers.add("AuthToken", twitterToken);

            // 创建请求实体
            HttpEntity<String> entity = new HttpEntity<>(headers);
            // 发送 GET 请求
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                // 解析响应数据
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                if (jsonObject == null) {
                    LogUtils.error("获取推特共同关注者接口数据返回异常 | response:", response.getBody());
                    return CompletableFuture.completedFuture(monitorUser);
                }
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
                                String userEntries = null;
                                for (int i = 0; i < instructionsArray.size(); i++) {
                                    JSONObject arrayJSONObject = instructionsArray.getJSONObject(i);
                                    if(arrayJSONObject.containsKey("entries")){
                                        userEntries = arrayJSONObject.getString("entries");
                                        break;
                                    }
                                }
                                if(StringUtils.isEmpty(userEntries)){
                                    LogUtils.error("获取推特共同关注者接口entries数据解析异常 | response:", response.getBody());
                                    return CompletableFuture.completedFuture(monitorUser);
                                }

                                //解析共同关注者列表
                                FollowerEntries followerEntries = JSON.parseObject(userEntries, FollowerEntries.class);
                                if(followerEntries == null || followerEntries.getEntries() == null || followerEntries.getEntries().isEmpty()){
                                    LogUtils.error("获取推特共同关注者接口 FollowerEntries数据解析异常 | response:", response.getBody());
                                    return CompletableFuture.completedFuture(monitorUser);
                                }
                                //提取列表中所有用户名
                                List<String> followersYouKnowList = new ArrayList<>();
                                for (FollowerInfo followerInfo : followerEntries.getEntries()) {
                                    FollowerLegacy followerLegacy = followerInfo.getContent().getItemContent().getUser_results().getResult().getLegacy();
                                    if(followerLegacy!=null){
                                        followersYouKnowList.add(followerLegacy.getScreenName());
                                    }
                                }
                                monitorUser.setFollowersYouKnowList(followersYouKnowList);
                                if(followersYouKnowList.isEmpty()){
                                    LogUtils.error("获取推特共同关注者接口 提取共同关注列表异常 | response:", response.getBody());
                                    return CompletableFuture.completedFuture(monitorUser);
                                }
                                Set<String> remarkUserKeySet = new HashSet<>(remarkUserKeyList); // 将 List 转换为 HashSet

                                List<String> resultList = new ArrayList<>(followersYouKnowList);
                                //仅保留共同关注者列表中包含备注列表中的元素
                                resultList.retainAll(remarkUserKeyList);

                                Set<String> matchedSet = followersYouKnowList.parallelStream()
                                        .filter(remarkUserKeySet::contains)
                                        .collect(Collectors.toSet());

                                Set<String> intersectSet = followersYouKnowList.parallelStream().filter(
                                        item-> remarkUserKeySet.contains(item)).collect(Collectors.toSet());

                                monitorUser.setFollowersYouKnowRemarkSet(matchedSet);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            LogUtils.error("获取推特共同关注者接口异常-请求地址: {}", url, e);
            if (response != null){
                LogUtils.error("response: {}", response.getBody(), e);
            }
        }
        return CompletableFuture.completedFuture(monitorUser);
    }

    public MonitorCoin getMonitorCoinInfo(String ca) {
        LogUtils.info("getMonitorCoinInfo-异步执行: {}", ca);
        String marketValueUrl = "https://gmgn.ai/defi/quotation/v1/sol/tokens/realtime_token_price?address="+ca+"&decimals="+ca;
        String launchpadUrl = "https://gmgn.ai/api/v1/token_launchpad_info/sol/"+ca;
        String coinNameUrl = "https://gmgn.ai/api/v1/token_info/sol/"+ca;
        // 创建HttpHeaders对象
        HttpHeaders headers = new HttpHeaders();
        // 添加请求头
        headers.add("Referer", "https://gmgn.ai/sol/token/"+ca);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        MonitorCoin coin = new MonitorCoin();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(marketValueUrl, HttpMethod.GET, entity, String.class);
            if (response != null){
                String responseBody = response.getBody();
                LogUtils.info("获取代币市值信息成功: {}", responseBody);
                org.json.JSONObject jsonObject = new org.json.JSONObject(responseBody);
                double aDouble = jsonObject.getJSONObject("data").getDouble("usd_price");
                double convertedYuan = aDouble * 1000000000;
                // 设置格式化规则，保留两位小数
                DecimalFormat df = new DecimalFormat("#,##0.00");
                String formattedYuan = df.format(convertedYuan);
                coin.setMarketValue("$"+formattedYuan);
            } else {
                LogUtils.error("获取代币信息失败: {}", ca);
            }
        } catch (Exception e) {
            LogUtils.error("获取代币信息失败: {}", ca, e);
            if (response != null){
                LogUtils.error("response: {}", response.getBody(), e);
            }
        }

        try {
            response = restTemplate.exchange(launchpadUrl, HttpMethod.GET, entity, String.class);
            if (response != null){
                String responseBody = response.getBody();
                LogUtils.info("获取代币进度信息成功: {}", responseBody);
                org.json.JSONObject jsonObject = new org.json.JSONObject(responseBody);
                String progress = jsonObject.getJSONObject("data").getString("launchpad_progress");
                double value = Double.parseDouble(progress);
                int percentage = (int) Math.round(value * 100);
                coin.setCoinLaunchpad(percentage+"%");
            } else {
                LogUtils.error("获取代币进度信息失败: {}", ca);
            }
        } catch (Exception e) {
            LogUtils.error("获取代币进度信息失败: {}", ca, e);
            if (response != null){
                LogUtils.error("response: {}", response.getBody(), e);
            }
        }

        try {
            response = restTemplate.exchange(coinNameUrl, HttpMethod.GET, entity, String.class);
            if (response != null){
                String responseBody = response.getBody();
                LogUtils.info("获取代币名称信息成功: {}", responseBody);
                org.json.JSONObject jsonObject = new org.json.JSONObject(responseBody);
                String name = jsonObject.getJSONObject("data").getString("name");
                coin.setCoinName(name);
            } else {
                LogUtils.error("获取代币名称信息失败: {}", ca);
            }
        } catch (Exception e) {
            LogUtils.error("获取代币名称信息失败: {}", ca, e);
            if (response != null){
                LogUtils.error("response: {}", response.getBody(), e);
            }
        }

        return coin;
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
                    LogUtils.error("解析推文-裁剪至末尾字符串: {}", pumpCa);
                    //return result;
                }
                if(StringUtils.isEmpty(pumpCa)){
                    LogUtils.error("解析ca异常: {}", fullText[0]);
                    return result;
                }
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
                    for (String mentionUserId : mentionUserList) {
                        //匹配ca提及用户
                        if(mentionUserId.equals(user.getUserID())){
                            LogUtils.info("该ca用户已提及: {} ", pumpCa + " | "+user.getUserName());
                            return result;
                        }
                    }
                    //添加用户提及ca记录
                    mentionUserList.add(user.getUserID());
                }

                String coinJson = JSON.toJSONString(coin);
                redisCache.setCacheObject(pumpCa, coinJson, 3, TimeUnit.DAYS);
                //拼接tg消息发送

                StringBuilder messageBuilder = new StringBuilder(); // 使用 StringBuilder 进行拼接
                //提及次数大于1

                //命中备注列表
                if(!StringUtils.isEmpty(user.getUserRemark())){
                    messageBuilder.append("⚡\uFE0F 命中备注列表: ").append(user.getUserShowName()).append("\n");
                    messageBuilder.append("\n");
                }
                //粉丝数阶梯提示
                if(Long.parseLong(user.getFansNumber()) >10000){
                    messageBuilder.append("┌ ❗ 粉丝数大于1w").append("\n");
                }
                if(Long.parseLong(user.getFansNumber()) >20000){
                    messageBuilder.append("├ ❗❗ 粉丝数大于2w").append("\n");
                }
                if(Long.parseLong(user.getFansNumber()) >50000){
                    messageBuilder.append("├ ❗❗❗ 粉丝数大于5w").append("\n");
                }
                if(Long.parseLong(user.getFansNumber()) >100000){
                    messageBuilder.append("├ ❗❗❗❗ 粉丝数大于10w").append("\n");
                }
                if(Long.parseLong(user.getFansNumber()) >150000){
                    messageBuilder.append("├ ❗❗❗❗❗ 粉丝数大于15w").append("\n");
                }
                if(Long.parseLong(user.getFansNumber()) >200000){
                    messageBuilder.append("└ ❗❗❗❗❗❗ 粉丝数大于20w").append("\n");
                }
                if(coin.getMentionUserList().size()>1){
                    messageBuilder.append("\uD83D\uDD25 ca提及次数: ").append(coin.getMentionUserList().size()).append("\n");
                }
                messageBuilder.append("\n");

                //https://gmgn.ai/sol/token/3GD2FWYkG2QGXCkN1nEf9TB1jsvt2zvUUEKEmFfgpump
                messageBuilder.append("┌ ca: ").append("<code>").append(coin.getCoinCa()).append("</code>").append("\n");
                messageBuilder.append("├ gmgn: ").append("https://gmgn.ai/sol/token/").append(coin.getCoinCa()).append("\n");
                messageBuilder.append("└ pump: ").append("https://pump.fun/coin/").append(coin.getCoinCa()).append("\n");
                messageBuilder.append("\n");

//                MonitorCoin monitorCoinInfo = getMonitorCoinInfo(coin.getCoinCa());
//                LogUtils.info("代币信息：{}",monitorCoinInfo);
//                if (Objects.nonNull(monitorCoinInfo)){
//                    messageBuilder.append("ca名称: ").append(monitorCoinInfo.getCoinName()).append("\n");
//                    messageBuilder.append("市值: ").append(monitorCoinInfo.getMarketValue()).append("\n");
//                    messageBuilder.append("进度: ").append(monitorCoinInfo.getCoinLaunchpad()).append("\n");
//                }

                //messageBuilder.append("ca名称: ").append(pumpCa).append("\n");
                //messageBuilder.append("ca创建时间: ").append(pumpCa).append("\n");

                messageBuilder.append("┌ twitter: ").append(tweetUrl).append("\n");
                messageBuilder.append("├ 作者: ").append(user.getUserName()).append("\n");
                messageBuilder.append("├ 粉丝数: ").append(user.getFansNumber()).append("\n");
                messageBuilder.append("└ 是否认证: ").append(user.getIsCertified()).append("\n");
                messageBuilder.append("\n");

                if (StringUtils.isNotEmpty(user.getUserRemark())) {
                    JSONArray remarksArray = JSON.parseArray(user.getUserRemark());
                    messageBuilder.append(remarksArray.stream()
                            .map(remark -> {
                                String symbol;
                                int index = remarksArray.indexOf(remark);
                                 if (index == remarksArray.size() - 1) {
                                    symbol = "└ ";
                                } else if (index == 0) {
                                    symbol = "┌ ";
                                }  else {
                                    symbol = "├ ";
                                }
                                return symbol + "用户备注: " + remark;
                            })
                            .collect(Collectors.joining("\n")));
                    messageBuilder.append("\n");
                }

                //拼接共同关注者命中备注列表信息
                if (user.getFollowersYouKnowRemarkSet()!=null && !user.getFollowersYouKnowRemarkSet().isEmpty()) {
                    user.getFollowersYouKnowRemarkSet().parallelStream()
                            .filter(redisCache::hasKey) // 仅保留在缓存中的键
                            .map(redisCache::getCacheObject) // 获取用户字符串
                            .map(Object::toString) // 转换为字符串
                            .map(userString -> JSON.parseObject(userString, MonitorUser.class)) // 转换为 MonitorUser 对象
                            .filter(remarkUser -> !StringUtils.isEmpty(remarkUser.getUserRemark())) // 过滤掉没有备注的用户
                            .flatMap(remarkUser -> {
                                JSONArray remarksArray = JSON.parseArray(remarkUser.getUserRemark());
                                return remarksArray.stream()
                                        .map(userRemark -> {
                                            String symbol;
                                            int index = remarksArray.indexOf(userRemark);
                                            if (index == remarksArray.size() - 1) {
                                                symbol = "└ ";
                                            } else if (index == 0) {
                                                symbol = "┌ ";
                                            } else {
                                                symbol = "├ ";
                                            }
                                            return symbol + "关注者备注: " + userRemark + "\n";
                                        });
                            })
                            .forEach(messageBuilder::append); // 将结果添加到 messageBuilder
                }

                messageBuilder.append("\n");

                messageBuilder.append("┌发布时间: ").append(DateHandleUtil.formatDate(createdDate)).append("\n");
                //messageBuilder.append("搜索时间: ").append(nowTime).append("\n");
                messageBuilder.append("└ 推送时间: ").append(DateUtils.getTime()).append("\n");

                //增加dream-bot机器人
                telegramDreamBot.sendText(messageBuilder.toString());
                telegramBot.sendText(messageBuilder.toString());
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
        } catch (Exception e) {
            LogUtils.error("解析用户黑名单异常", e);
            e.printStackTrace();
        }
        LogUtils.info("初始化解析用户黑名单完成");
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
                            String userString = redisCache.getCacheObject(userNameKey);
                            user = JSON.parseObject(userString, MonitorUser.class);
                            if(!StringUtils.isEmpty(user.getUserRemark())){
                                JSONArray remarksArray = JSON.parseArray(user.getUserRemark());
                                if(remarksArray!=null){
                                    //增加新备注
                                    remarksArray.add(userDetails.getString("tag"));
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
    }
}