package com.web3.twitter;

import com.alibaba.fastjson2.JSON;
import com.web3.twitter.redis.RedisCache;
import com.web3.twitter.utils.DateUtils;
import com.web3.twitter.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    @Autowired
    private TwitterMonitor twitterMonitor;

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private static final List<String> CHAT_ID_LIST = new ArrayList<>();

    static {
//        CHAT_ID_LIST.add(-1002429995604");//超级重点频道
//        CHAT_ID_LIST.add(-1002472298104");//pump to the moon
//        CHAT_ID_LIST.add("-4694062162");//重点频道
        CHAT_ID_LIST.add("-4655003313");//pump扫推监控
        CHAT_ID_LIST.add("-1002250310542");//pump扫推监控备用
//        CHAT_ID_LIST.add("-1002358331062");//旧频道(免费公开)
//        CHAT_ID_LIST.add("-1002190498173");
//        CHAT_ID_LIST.add("7146351054");
//        CHAT_ID_LIST.add("-4517558084");
    }

    @Autowired
    private RedisCache redisCache;

    private final TelegramClient telegramClient;

    public static final Chat GROUP_CHAT = Chat
            .builder()
            .id(10L)
            .type("group")
            .title("My Group Chat")
            .build();

    public static final User TEST_USER = new User(
            1000L,
            "Test",
            false,
            "User",
            "testUser",
            "en",
            null,
            null,
            null,
            false,
            null,
            null,
            false
    );

    public TelegramBot() {
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    public void sendText(String textMoreImport, String textImport, String text, InlineKeyboardMarkup replyMarkup) {
//        log.info("发送消息参数: {}.", text);
//        if (!redisCache.hasKey("chat_id")){
//            LogUtils.error("没有获取到chatID: {}.", text);
//            //设置默认聊天Id
//            List<String> chatIds = new ArrayList<>();
//            chatIds.add("-1002270508207");
//            redisCache.setCacheList("chat_id", chatIds);
//        }
        //List<String> chatIdList = redisCache.getCacheList("chat_id");

        if(StringUtils.isNotEmpty(textImport) && textImport.length()>2) {
            SendMessage methodImport = new SendMessage("-1002472298104", textImport);
            methodImport.setParseMode("HTML");
            methodImport.setDisableWebPagePreview(true);
            if(replyMarkup!=null){
                methodImport.setReplyMarkup(replyMarkup);
            }
            Message responseMessageImport = new Message();
            responseMessageImport.setChat(GROUP_CHAT);
            responseMessageImport.setFrom(TEST_USER);
            responseMessageImport.setText(textImport);
            //responseMessage.setReplyMarkup(replyMarkup);
            Message parsedMessageImport = new Message();
            try {
                telegramClient.execute(methodImport);
            } catch (TelegramApiException e) {
                LogUtils.error("Shiyi-bot重点频道发送消息异常: %s %s", parsedMessageImport, e);
                e.printStackTrace();
            }
        }

        //超级重点频道
        if(StringUtils.isNotEmpty(textMoreImport) && textMoreImport.length()>2){
            SendMessage methodImport = new SendMessage("-1002429995604", textMoreImport);
            methodImport.setParseMode("HTML");
            methodImport.setDisableWebPagePreview(true);
            if(replyMarkup!=null){
                methodImport.setReplyMarkup(replyMarkup);
            }
            Message responseMessageImport = new Message();
            responseMessageImport.setChat(GROUP_CHAT);
            responseMessageImport.setFrom(TEST_USER);
            responseMessageImport.setText(textMoreImport);
            //responseMessage.setReplyMarkup(replyMarkup);
            Message parsedMessageImport = new Message();
            try {
                telegramClient.execute(methodImport);
            } catch (TelegramApiException e) {
                LogUtils.error("Shiyi-bot-pump to the moon频道发送消息异常: %s %s.", parsedMessageImport, e);
                e.printStackTrace();
            }
        }

        //重点频道
        if(StringUtils.isNotEmpty(textImport) && textImport.length()>2){
            SendMessage methodImport = new SendMessage("-4694062162", textImport);
            methodImport.setParseMode("HTML");
            methodImport.setDisableWebPagePreview(true);
            if(replyMarkup!=null){
                methodImport.setReplyMarkup(replyMarkup);
            }
            Message responseMessageImport = new Message();
            responseMessageImport.setChat(GROUP_CHAT);
            responseMessageImport.setFrom(TEST_USER);
            responseMessageImport.setText(textImport);
            //responseMessage.setReplyMarkup(replyMarkup);
            Message parsedMessageImport = new Message();
            try {
                telegramClient.execute(methodImport);
            } catch (TelegramApiException e) {
                LogUtils.error("Shiyi-bot重点频道发送消息异常: %s %s.", parsedMessageImport, e);
                e.printStackTrace();
            }

            /*try {
                //等待1.5s后发送其他频道
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LogUtils.error("Shiyi-bot发送消息暂停异常: {}.", text, e);
                e.printStackTrace();
            }*/
        }

        CHAT_ID_LIST.forEach(chatID -> {
            SendMessage method = new SendMessage(chatID, text);
            method.setParseMode("HTML");
            method.setDisableWebPagePreview(true);
            if(replyMarkup!=null){
                method.setReplyMarkup(replyMarkup);
            }
            Message responseMessage = new Message();
            responseMessage.setChat(GROUP_CHAT);
            responseMessage.setFrom(TEST_USER);
            responseMessage.setText(text);
            //responseMessage.setReplyMarkup(replyMarkup);
            Message parsedMessage = new Message();
            try {
                telegramClient.execute(method);
            } catch (TelegramApiException e) {
                LogUtils.error("Shiyi-bot发送消息异常: %s %s", parsedMessage, e);
                e.printStackTrace();
            }
        });
        LogUtils.info("sendText-bot发送消息完成: {}", DateUtils.getTimeSSS());
    }

    /**
     * 普通频道延迟推送消息
     * @param text
     * @param replyMarkup
     */
    public void sendPublicText(String text, InlineKeyboardMarkup replyMarkup) {
//        log.info("发送消息参数: {}.", text);
//        if (!redisCache.hasKey("chat_id")){
//            LogUtils.error("没有获取到chatID: {}.", text);
//            //设置默认聊天Id
//            List<String> chatIds = new ArrayList<>();
//            chatIds.add("-1002270508207");
//            redisCache.setCacheList("chat_id", chatIds);
//        }
        //List<String> chatIdList = redisCache.getCacheList("chat_id");

        if(StringUtils.isNotEmpty(text) && text.length()>2) {
            SendMessage methodImport = new SendMessage("-1002358331062", text);
            methodImport.setParseMode("HTML");
            methodImport.setDisableWebPagePreview(true);
            if(replyMarkup!=null){
                methodImport.setReplyMarkup(replyMarkup);
            }
            Message responseMessageImport = new Message();
            responseMessageImport.setChat(GROUP_CHAT);
            responseMessageImport.setFrom(TEST_USER);
            responseMessageImport.setText(text);
            //responseMessage.setReplyMarkup(replyMarkup);
            Message parsedMessageImport = new Message();
            try {
                telegramClient.execute(methodImport);
            } catch (TelegramApiException e) {
                LogUtils.error("Shiyi-bot普通频道发送消息异常: {}.", parsedMessageImport, e);
                e.printStackTrace();
            }
        }
        LogUtils.info("sendText-bot发送消息完成: {}", DateUtils.getTimeSSS());
    }

    @Override
    public String getBotToken() {
        return "7907041466:AAF3wj6mI5-XH00a2k75lfBIAEmLIdSNOeQ";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            LogUtils.info("consume回调message_text: {}", message_text);
            LogUtils.info("consume回调chatID: {}", chat_id);
            //把群聊id放入redis
//            if (!redisCache.hasKey("chat_id")){
//                LogUtils.info("redis不存在聊天id");
//                List<String> chatIdList = new ArrayList<>();
//                chatIdList.add(String.valueOf(chat_id));
//                redisCache.setCacheList("chat_id", chatIdList);
//                LogUtils.info("添加成功！！");
//            } else {
//                LogUtils.info("redis存在聊天id");
//                List<String> chatIdList = redisCache.getCacheList("chat_id");
//                LogUtils.info("chatIds列表: {}", chatIdList);
//                if (!chatIdList.contains(String.valueOf(chat_id))){
//                    chatIdList.add(String.valueOf(chat_id));
//                    redisCache.setCacheList("chat_id", chatIdList);
//                }
//            }
            LogUtils.info("收到消息.... message_text=" + message_text + " | chat_id=" + chat_id);
            SendMessage message = SendMessage // Create a message object
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .build();
//            try {
//                telegramClient.execute(message); // Sending our message object to user
//            } catch (TelegramApiException e) {
//                LogUtils.error("回复消息失败: {}.", message, e);
//                e.printStackTrace();
//            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        LogUtils.info("Shiyi机器人注册成功: {}.", botSession.toString());
    }
}