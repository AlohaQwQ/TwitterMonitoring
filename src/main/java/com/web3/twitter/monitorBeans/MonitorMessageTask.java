package com.web3.twitter.monitorBeans;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * 普通频道延迟消息任务Bean
 */
public class MonitorMessageTask {

    /**
     * 名称
     */
    private String name;

    /**
     * 消息
     */
    private String message;

    /**
     * 键盘内容
     */
    private InlineKeyboardMarkup inlineKeyboardMarkup;

    /**
     * 创建时间
     */
    private String createDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public void setInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup) {
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MonitorMessageTask{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", inlineKeyboardMarkup='" + inlineKeyboardMarkup + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
