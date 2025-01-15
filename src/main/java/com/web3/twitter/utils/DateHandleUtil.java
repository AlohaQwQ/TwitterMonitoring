package com.web3.twitter.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.ZonedDateTime;

/**
 * 时间处理工具
 */
public class DateHandleUtil {

    /**
     * 获取指定往前推的日期
     *
     * @return
     */
    public static String getDataTime(int days) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 根据传进来的天数获取往前推的日期
        LocalDate yesterday = today.minusDays(days);
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 格式化期
        String formattedYesterday = yesterday.format(formatter);
        // 返回日期
        return formattedYesterday;
    }

    public static String getDataTimeYYDD(int days) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 根据传进来的天数获取往前推的日期
        LocalDate yesterday = today.minusDays(days);
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 格式化期
        String formattedYesterday = yesterday.format(formatter);
        // 返回日期
        return formattedYesterday;
    }

    public static String getDataTimeYYMM(int days) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 根据传进来的天数获取往前推的日期
        LocalDate yesterday = today.minusDays(days);
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 格式化期
        String formattedYesterday = yesterday.format(formatter);
        // 返回日期
        return formattedYesterday;
    }

    /**
     * 将 yyyyMMdd 格式的日期字符串转换为 Date 对象
     *
     * @param dateStr 输入的日期字符串，格式为 yyyyMMdd
     * @return 转换后的 Date 对象
     */
    public static Date convertToDate(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            // 使用LocalDate解析日期字符串
            LocalDate date = LocalDate.parse(dateStr, inputFormatter);
            // 转换为 Date 对象
            return java.sql.Date.valueOf(date);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertToDate2(String dateStr) {
        // 使用 DateTimeFormatterBuilder 创建自定义解析器
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .appendPattern("E MMM dd HH:mm:ss")
                .optionalStart()
                .appendZoneId()
                .optionalEnd()
                .appendPattern(" yyyy")
                .toFormatter();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            // 解析输入字符串
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, inputFormatter);
            // 转换为 UTC+8 的中国时间
            ZonedDateTime chinaTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
            // 格式化并返回目标字符串
            return chinaTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return dateStr; // 如果解析失败，返回原始字符串
        }
    }

    public static String formatDate(String date) {
        if (date == null) {
            return null;
        }
        String[] s = date.split(" ");
        String result = s[s.length - 1] + "-" +
                convertToChineseMonth(s[1]) + "-" +
                s[2] + " " + s[3];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateTime = formatter.parse(result);
            // 获取Calendar实例并设置日期时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);

            // 加上八个小时
            calendar.add(Calendar.HOUR_OF_DAY, 8);

            // 获取新的日期对象
            Date newDateTime = calendar.getTime();

            // 将新的日期对象格式化为字符串
            return formatter.format(newDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertToChineseMonth(String englishMonth) {
        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");

        return monthMap.getOrDefault(englishMonth, "未知月份");
    }

    /**
     * 将时间戳转换为 Date 对象
     *
     * @param timestamp 时间戳（毫秒）
     * @return 转换后的 Date 对象
     */
    public static Date convertTimestampToDate(long timestamp) {
        // 将时间戳转换为 Date 对象并返回
        return new Date(timestamp);
    }


    /**
     * 将字符串转换为 URL 编码
     *
     * @param str 待编码的字符串
     * @return
     */
    public static String encode(String str) {
        try {
            // 使用UTF-8字符集进行URL编码
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // 如果出现编码异常，返回null
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定往前推的日期的零点时间戳（秒级别）
     *
     * @param days 往前推的天数
     * @return 往前推的日期零点的时间戳（秒级别）
     */
    public static long getMidnightTimestamp(int days) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 根据传进来的天数获取往前推的日期
        LocalDate targetDate = today.minusDays(days);
        // 将日期设置为零点时间
        LocalDateTime midnight = targetDate.atStartOfDay();
        // 将 LocalDateTime 转换为时间戳，并转换为秒级别
        long timestamp = midnight.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        // 返回零点时间的秒级别时间戳
        return timestamp;
    }


    /**
     * 将日期从 yyyy/MM/dd 格式转换为 Date 对象
     *
     * @param dateStr 输入的日期字符串，格式为 yyyy/MM/dd
     * @return 转换后的 Date 对象
     * @throws ParseException 如果输入的日期格式不正确，会抛出解析异常
     */
    public static Date convertStringToDate(String dateStr){
        // 定义输入日期格式
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");

        // 将字符串解析为 Date 对象并返回
        try {
            return inputFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定往前推的日期时间戳
     *
     * @return
     */
    public static long getMinDataTimeStamp(int days) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 根据传进来的天数获取往前推的日期
        LocalDate yesterday = today.minusDays(days);

        // 获取时间戳
        return yesterday.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

//    public static void main(String[] args) throws InterruptedException {
//        String releaseTime = DateUtils.getTime();
//        Thread.sleep(6000);
//        String pushTime = DateUtils.getTime();
//        String newReleaseTime = DateHandleUtil.calculateDifferenceInSeconds(releaseTime, pushTime);
//        LogUtils.info("pushTime:{} releaseTime:{} newReleaseTime:{} nowTime:{}", pushTime, releaseTime, newReleaseTime, DateUtils.getTime());
//    }

    /**
     * 计算两个时间之间的秒数差
     *
     * @param startTime 开始时间（字符串格式）
     * @param endTime   结束时间（字符串格式）
     * @return 两个时间之间的秒数差
     */
    public static String calculateDifferenceInSeconds(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将时间字符串解析为 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);

        // 计算两个时间之间的秒数差
        long between = ChronoUnit.SECONDS.between(startDateTime, endDateTime);
        long seconds = 0;
        if (between >= 5){
            //取值3-5之间整数的随机数，3 4 5
            Random random = new Random();
            int randomNumber = 3 + random.nextInt(3);
            seconds = between - randomNumber;
            startDateTime = startDateTime.plusSeconds(seconds);
            startTime = startDateTime.format(formatter);
        }

        return startTime;
    }



}
