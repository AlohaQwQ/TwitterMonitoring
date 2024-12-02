package com.web3.twitter;

import com.web3.twitter.bot.MyAmazingBot;
import com.web3.twitter.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TwitterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterApplication.class, args);

        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        try {
            // Register our bot
            String botToken = "7816953388:AAFDf_a2OgsWIAWgPjWc54P8eHtZYMJdQb4";
            botsApplication.registerBot(botToken, new TelegramDreamBot());
            LogUtils.info("注册机器人dream_gg_bot.....");

            String botTokenShiyi = "7907041466:AAF3wj6mI5-XH00a2k75lfBIAEmLIdSNOeQ";
            botsApplication.registerBot(botTokenShiyi, new TelegramBot());
            LogUtils.info("注册机器人shiyi_bot.....");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
