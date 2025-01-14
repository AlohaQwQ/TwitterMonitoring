package com.web3.twitter;

import com.web3.twitter.utils.LogUtils;
import com.web3.twitter.utils.TelegramDongBot;
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
//            String botTokenShiyi = "7907041466:AAF3wj6mI5-XH00a2k75lfBIAEmLIdSNOeQ";
//            botsApplication.registerBot(botTokenShiyi, new TelegramBot());
//            LogUtils.info("注册机器人shiyi_bot.....");

            String botToken = "7719750548:AAEqoOVqnOlShBRNsWvpyInjSCVnKkk9jNg";
            botsApplication.registerBot(botToken, new TelegramDongBot());
            LogUtils.info("注册机器人dong_bot.....");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
