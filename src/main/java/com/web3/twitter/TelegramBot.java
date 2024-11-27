package com.web3.twitter;

import com.web3.twitter.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

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

    public void sendText(String text) {
        log.info("发送消息参数: {}.", text);
        SendMessage method = new SendMessage("1002270508207", text);
        Message responseMessage = new Message();
        responseMessage.setChat(GROUP_CHAT);
        responseMessage.setFrom(TEST_USER);
        responseMessage.setText(text);
        Message parsedMessage = new Message();
        try {
            parsedMessage = telegramClient.execute(method); // Sending our message object to user
        } catch (TelegramApiException e) {
            log.error("发送消息异常: {}.", parsedMessage,e);
            e.printStackTrace();
        }
        log.info("返回消息: {}.", parsedMessage);
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

            log.info("message_text={},chat_id={}",message_text,chat_id);

            SendMessage message = SendMessage // Create a message object
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .build();
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                log.error("回复消息失败：{}",message,e);
                e.printStackTrace();
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
        log.info("机器人注册成功！Registered bot running state is={} ",botSession.isRunning());
    }
}