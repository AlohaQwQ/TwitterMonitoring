package com.web3.twitter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.web3.twitter.monitor.CustomDateDeserializer;
import com.web3.twitter.utils.LogUtils;
import com.web3.twitter.twitterBeans.*;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Component
public class TwitterMonitor {

    private final RestTemplate restTemplate;
    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";

    public TwitterMonitor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void startMonitor(){
        String url = String.format("https://twitter-x.p.rapidapi.com/lists/tweets?list_id=1649315100527046658&count=2");
        String cookies = "";
        // 创建请求头并添加 cookies
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add(HttpHeaders.COOKIE, cookies);
        headers.add("x-rapidapi-host", "twitter-x.p.rapidapi.com");
        headers.add("x-rapidapi-key", "c7d7d10b34msh8a76b09b95a1e87p1ff1dcjsn0d20ab44ecb6");

        // 创建请求实体
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送 GET 请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 处理响应数据
        if (response.getStatusCode() == HttpStatus.OK) {
            // 解析响应数据
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            if(jsonObject==null){
                LogUtils.error("监控数据返回异常 | url:", url);
            }
            // 获取应用列表
            JSONObject data = jsonObject.getJSONObject("data");
            if(data.containsKey("list")){

                //解析推特列表对象
                TwitterList twitterList = JSON.parseObject(data.getString("list"), TwitterList.class);
                if(twitterList!=null){
                    try {
                        if(twitterList.getTweets_timeline().getTimeline().getInstructions()!=null &&
                                !twitterList.getTweets_timeline().getTimeline().getInstructions().isEmpty()){
                            List<Instructions> instructionsList = twitterList.getTweets_timeline().getTimeline().getInstructions();
                            for (Instructions instruction : instructionsList) {
                                List<Entries> entriesList = instruction.getEntries();
                                if(entriesList!=null && !entriesList.isEmpty()){
                                    for (Entries entries : entriesList) {
                                        Result result = entries.getContent().getItemContent().getTweet_results().getResult();
                                        String restId = result.getRest_id();
                                        LogUtils.info("解析推特列表-推特restId: {}.", restId);
                                        User_results userResults = result.getCore().getUser_results();
                                        LogUtils.info("解析推特列表-userResults: {}.", userResults);
                                        LogUtils.info("解析推特列表-用户restId: {}.", userResults.getResult().getRest_id());
                                        Quoted_status_result quoted_status_result = result.getQuoted_status_result();
                                        Result quoted_status_result_result = quoted_status_result.getResult();
                                        Legacy legacy = quoted_status_result_result.getLegacy();
                                        Date createdDate = legacy.getCreated_at();
                                        String fullText = legacy.getFull_text();
                                        //解析用户推文
                                    }
                                }
                            }
                        }
                    } catch (Exception e){
                        LogUtils.error("解析推特列表数据异常-请求地址: {}.", url, e);
                    }
                }
            }
        }

    }
}