package com.web3.twitter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.web3.twitter.monitor.CustomDateDeserializer;
import com.web3.twitter.monitorBeans.MonitorUser;
import com.web3.twitter.redis.RedisCache;
import com.web3.twitter.utils.LogUtils;
import com.web3.twitter.twitterBeans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Component
public class TwitterMonitor {

    @Autowired
    private RedisCache redisCache;

    private final RestTemplate restTemplate;
    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";

    public TwitterMonitor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String startMonitor(){
        String url = "https://twitter-x.p.rapidapi.com/lists/tweets?list_id=1771221106613187071&count=2";
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-rapidapi-host", "twitter-x.p.rapidapi.com");
        headers.add("x-rapidapi-key", "c7d7d10b34msh8a76b09b95a1e87p1ff1dcjsn0d20ab44ecb6");
        ResponseEntity<String> response = null;
        // 创建请求实体
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            // 发送 GET 请求
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e){
            LogUtils.error("请求聚合api异常-请求地址: {}.", url, e);
        }
        String fullText = "";
        // 处理响应数据
        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            try {
                // 解析响应数据
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                if (jsonObject == null) {
                    LogUtils.error("推特数据返回异常 | url:", url);
                }
                // 获取应用列表
                JSONObject data = jsonObject.getJSONObject("data");
                if (data.containsKey("list")) {
                    //解析推特列表对象
                    TwitterList twitterList = JSON.parseObject(data.getString("list"), TwitterList.class);
                    if (twitterList != null) {

                        if (twitterList.getTweets_timeline().getTimeline().getInstructions() != null &&
                                !twitterList.getTweets_timeline().getTimeline().getInstructions().isEmpty()) {
                            List<Instructions> instructionsList = twitterList.getTweets_timeline().getTimeline().getInstructions();
                            for (Instructions instruction : instructionsList) {
                                List<Entries> entriesList = instruction.getEntries();
                                if (entriesList != null && !entriesList.isEmpty()) {
                                    for (Entries entries : entriesList) {
                                        Content content = entries.getContent();
                                        if (content != null) {
                                            ItemContent itemContent = content.getItemContent();
                                            if (itemContent != null) {
                                                Tweet_results tweet_results = itemContent.getTweet_results();
                                                if (tweet_results != null) {
                                                    Result result = tweet_results.getResult();
                                                    if (result != null) {
                                                        String restId = result.getRest_id();
                                                        LogUtils.info("解析推特列表-推特restId: {}.", restId);
                                                        //解析用户相关
                                                        User_results userResults = result.getCore().getUser_results();
                                                        if (userResults != null) {
                                                            LogUtils.info("解析推特列表-userResults: {}.", userResults);
                                                            String userID = userResults.getResult().getRest_id();
                                                            LogUtils.info("解析推特列表-用户restId: {}.", userID);
                                                            //拼接推特链接 https://x.com/VT_BNB/status/1861334062021185655
                                                            String userName = userResults.getResult().getLegacy().getScreen_name();
                                                            String tweetUrl = String.format("https://x.com/%s/status/%s", userName, restId);
                                                            LogUtils.info("解析推特列表-推特链接: {}.", tweetUrl);

                                                            //存储用户信息
                                                            if(!redisCache.hasKey(userID)){
                                                                MonitorUser user = new MonitorUser();
                                                                user.setUserID(userID);
                                                                user.setUserName(userName);
                                                                user.setFansNumber("100");
                                                                user.setIsCertified("0");
                                                                String jsonUser = JSON.toJSONString(user);
                                                                redisCache.setCacheObject(userID, jsonUser);
                                                            }

                                                        }

                                                        Legacy legacy = result.getLegacy();
                                                        String createdDate = legacy.getCreated_at();
                                                        LogUtils.info("解析推特列表-createdDate: {}.", createdDate);
                                                        fullText = legacy.getFull_text();
                                                        LogUtils.info("解析推特列表-推文: {}.", fullText);
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
                LogUtils.error("解析推特列表数据异常-请求地址: {}.", url, e);
            }
        }
        return fullText;
    }
}