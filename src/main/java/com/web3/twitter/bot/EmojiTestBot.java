package com.web3.twitter.bot;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 带表情符号
 */
public class EmojiTestBot implements LongPollingSingleThreadUpdateConsumer {

    //创建客户端
    private final TelegramClient telegramClient;

    public EmojiTestBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }


    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            long user_id = update.getMessage().getChat().getId();
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            //添加表情
            String answer = EmojiParser.parseToUnicode("这是一个微笑表情: :smile:");
            SendMessage message = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(answer)
                    .build();
            log(user_first_name, user_last_name, Long.toString(user_id), message_text, message_text);
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }


}
