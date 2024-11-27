package com.web3.twitter;

import com.web3.twitter.bot.MyAmazingBot;
import com.web3.twitter.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
public class TwitterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterApplication.class, args);

        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        // Register our bot
        String botToken = "7907041466:AAF3wj6mI5-XH00a2k75lfBIAEmLIdSNOeQ";
        try {
            botsApplication.registerBot(botToken, new TelegramBot());
            LogUtils.info("注册机器人.....");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
