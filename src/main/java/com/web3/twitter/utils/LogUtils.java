package com.web3.twitter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理并记录日志文件
 * 
 * @author ruoyi
 */
public class LogUtils {

    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

    public static String getBlock(Object msg) {
        return msg == null ? "null" : "[" + msg.toString() + "]";
    }

    private static String getFormattedMessage(String level, Object msg) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return String.format("[%s] [%s] [%s] %s", timestamp, level, getCallerInfo(), getBlock(msg));
    }

    private static String getFormattedMessage(String level, String label,Object msg) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return String.format("[%s] [%s] [%s] [%s] %s", timestamp, level, getCallerInfo(), label, getBlock(msg));
    }

    private static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[4].getClassName() + "." + stackTrace[4].getMethodName() + ":" + stackTrace[4].getLineNumber();
    }

    public static void error(String label, Object msg) {
        log.error(getFormattedMessage("ERROR", label, msg));
    }

    public static void error(String var1, Object var2, Object var3) {
        log.error(var1, var2, var3);
    }

    public static void info(String label,Object msg) {
        log.info(getFormattedMessage("INFO", label, msg));
    }

    public static void error(Object msg) {
        log.error(getFormattedMessage("ERROR", msg));
    }

    public static void info(Object msg) {
        log.info(getFormattedMessage("INFO", msg));
    }

    public static void warn(Object msg) {
        log.warn(getFormattedMessage("WARN", msg));
    }

    public static void debug(Object msg) {
        log.debug(getFormattedMessage("DEBUG", msg));
    }

    public static void trace(Object msg) {
        log.trace(getFormattedMessage("TRACE", msg));
    }
}
