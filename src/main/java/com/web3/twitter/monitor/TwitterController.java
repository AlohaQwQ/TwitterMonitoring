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
        //MonitorCoin monitorCoinInfo = twitterMonitor.getMonitorCoinInfo("FaBkQy6t1iuWt3ynEsUzFsa2mhpkLW7bGwR1id5Hpump");
        //System.out.println("monitorCoinInfo = " + monitorCoinInfo);
        return "0";
    }

    public static void main(String[] args) {
        String input = "night_mode=2; kdt=ZYDEnxT6LaHB6UMR8rae6uEkn6b6L0uM8dteXGUF; dnt=1; g_state={\"i_l\":3,\"i_p\":1737111564311}; gt=1878711948579401782; guest_id=v1%3A173675502339865648; guest_id_marketing=v1%3A173675502339865648; guest_id_ads=v1%3A173675502339865648; auth_token=2b5cd44cb177959e54ae4cec5cb13245c2ea796f; ct0=29acbfdcaeaab726f93ad9246ebbd84524c8b2a8074a799574dc1199becf13406f2ee00499f39445974fec91795f6fc6928a62e714a18a420a2acee4e5fb8a0e25a6ba78c5f1ee5bde20b59b6fa0a86a; twid=u%3D2176427736; att=1-KHDglsT6K2EArzmq1HUZhGJ2vEcQXay2tmToUp8M; personalization_id=\"v1_XzLy6TGJxDLZ0ULHgds54w==\"; lang=zh-cn";

        String escaped = input.replace("\"", "\\\"");
        System.out.println(escaped);
    }




}
