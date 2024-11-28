package com.web3.twitter.telegram;

import com.web3.twitter.TelegramBot;
import com.web3.twitter.TwitterMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 游戏信息Controller
 * 
 * @author hongyuan
 * @date 2024-08-29
 */
@RestController
@RequestMapping("/telegram")
public class TelegramController {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TwitterMonitor twitterMonitor;

    @GetMapping("/sendMessage")
    public String sendMessage() {
        twitterMonitor.startMonitor();
        return "0";
    }




}
