package com.web3.twitter.monitor;

import com.alibaba.fastjson2.JSON;
import com.web3.twitter.TwitterMonitor;
import com.web3.twitter.monitorBeans.MonitorCoin;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;

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
        MonitorCoin monitorCoinInfo = twitterMonitor.getMonitorCoinInfo("FaBkQy6t1iuWt3ynEsUzFsa2mhpkLW7bGwR1id5Hpump");
        System.out.println("monitorCoinInfo = " + monitorCoinInfo);
        return "0";
    }




}
