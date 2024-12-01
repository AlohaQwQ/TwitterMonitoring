package com.web3.twitter.monitor;

import com.alibaba.fastjson2.JSON;
import com.web3.twitter.TwitterMonitor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 游戏信息Controller
 * 
 * @author hongyuan
 * @date 2024-08-29
 */
@RestController
@RequestMapping("/twitter")
public class TwitterController {

    @Autowired
    private TwitterMonitor twitterMonitor;

    @GetMapping("/start")
    public String start() {
        twitterMonitor.startMonitor();
        return "0";
    }


    @GetMapping("/getCaInfo")
    public String getCaInfo() {
        twitterMonitor.getMonitorCoinInfo("4JWXK53NHxJWdZPAwdnZFNsRL7G1ewpoUpxT7WTppump");
        return "0";
    }

    public static void main(String[] args) {
        String data = "{\"data\":{\"id\":\"solana_4JWXK53NHxJWdZPAwdnZFNsRL7G1ewpoUpxT7WTppump\",\"type\":\"token\",\"attributes\":{\"address\":\"4JWXK53NHxJWdZPAwdnZFNsRL7G1ewpoUpxT7WTppump\",\"name\":\"Hardship Only Develops Leaders\",\"symbol\":\"H.O.D.L\",\"decimals\":6,\"image_url\":\"missing.png\",\"coingecko_coin_id\":null,\"total_supply\":\"999999998286346.0\",\"price_usd\":\"0.00000648\",\"fdv_usd\":\"6480.49\",\"total_reserve_in_usd\":\"4810.80461653279345453842637022\",\"volume_usd\":{\"h24\":\"47463.9844395585\"},\"market_cap_usd\":null},\"relationships\":{\"top_pools\":{\"data\":[{\"id\":\"solana_D8XwwRUdoBC7eZPWXdRU8xtXaWnZaaW3Gu83ZSGop3F2\",\"type\":\"pool\"}]}}}}";
        JSONObject jsonObject = new JSONObject(data);
        JSONObject attributes = jsonObject.getJSONObject("data").getJSONObject("attributes");
        String symbol = attributes.getString("symbol");
        System.out.println(symbol);
    }



}
