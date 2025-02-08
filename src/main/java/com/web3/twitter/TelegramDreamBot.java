package com.web3.twitter;

import com.web3.twitter.redis.RedisCache;
import com.web3.twitter.utils.DateUtils;
import com.web3.twitter.utils.LogUtils;
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
public class TelegramDreamBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private static final Logger log = LoggerFactory.getLogger(TelegramDreamBot.class);

    private static final List<String> CHAT_ID_LIST = new ArrayList<>();

    static {
        CHAT_ID_LIST.add("-1002190498173");
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

    public TelegramDreamBot() {
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    public void sendText(String text, InlineKeyboardMarkup replyMarkup) {
//        log.info("发送消息参数: {}.", text);
//        if (!redisCache.hasKey("chat_id")){
//            LogUtils.error("没有获取到chatID: {}.", text);
//            //设置默认聊天Id
//            List<String> chatIds = new ArrayList<>();
//            chatIds.add("-1002270508207");
//            redisCache.setCacheList("chat_id", chatIds);
//        }
        //List<String> chatIdList = redisCache.getCacheList("chat_id");
        CHAT_ID_LIST.forEach(chatID -> {
            SendMessage method = new SendMessage(chatID, text);
            method.setParseMode("HTML");
            method.setDisableWebPagePreview(true);
            method.setReplyMarkup(replyMarkup);
            Message responseMessage = new Message();
            responseMessage.setChat(GROUP_CHAT);
            responseMessage.setFrom(TEST_USER);
            responseMessage.setText(text);
            responseMessage.setReplyMarkup(replyMarkup);
            Message parsedMessage = new Message();
            try {
                telegramClient.execute(method);
            } catch (TelegramApiException e) {
                LogUtils.error("DreamBot发送消息异常: {}.", parsedMessage, e);
                e.printStackTrace();
            }
        });
        LogUtils.info("sendText-bot发送消息完成: {}", DateUtils.getTimeSSS());
    }

    @Override
    public String getBotToken() {
        return "7816953388:AAFDf_a2OgsWIAWgPjWc54P8eHtZYMJdQb4";
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
        LogUtils.info("Dream机器人注册成功: {}.", botSession.toString());
    }
}