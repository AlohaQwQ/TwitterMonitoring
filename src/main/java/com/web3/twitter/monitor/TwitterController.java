package com.web3.twitter.monitor;

import com.web3.twitter.TwitterMonitor;
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
        String ms = twitterMonitor.startMonitor();
        return ms;
    }




}
