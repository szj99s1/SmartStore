package com.daofu.commons.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author li-chuang
 * @date created in 2018/12/12 10:18
 * @description
 */
public final class DateUtils {

    /**
     * 获取当日开始时间
     * @return
     */
    public static LocalDateTime getLocalDateTimeStart(){
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 获取当日结束时间
     * @return
     */
    public static LocalDateTime getLocalDateTimeEnd(){
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }
    /**
     * 获取当日开始时间
     * @return
     */
    public static Date getDateStart(){
        return localDateTime2Date(getLocalDateTimeStart());
    }

    /**
     * 获取当日结束时间
     * @return
     */
    public static Date getDateEnd(){
        return localDateTime2Date(getLocalDateTimeEnd());
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * @description string日期格式转Date
     * @author lc
     * @date 2018/12/14 12:20
     * @param date
     * @param df
     * @return java.util.Date
     */
    public static Date stringDate2Date(String date, DateTimeFormatter df){
        LocalDateTime localDateTime = LocalDateTime.parse(date, df);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * @description string日期格式转Date
     * @author lc
     * @date 2018/12/14 12:20
     * @param date
     * @return java.util.Date
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * @description 当前时间格式化
     * @author lc
     * @date 2019-02-12 11:03
     * @param pattern
     * @return java.lang.String
     */
    public static String nowDateFormatByPattern(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @description 当前时间格式化
     * @author lc
     * @date 2019-02-12 11:03
     * @param date
     * @return java.lang.String
     */
    public static String date2String(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * @description 当前时间格式化
     * @author lc
     * @date 2019-02-12 11:03
     * @return java.lang.String
     */
    public static String nowDateFormat(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @description string转date
     * @author lc
     * @date 2019-03-12 15:47
     * @param date
     * @return java.util.Date
     */
    public static Date string2Date(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (Exception e){
        }
        return null;
    }

    public static String dateFormart(Date date){
        return date2LocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String stringDateFormartString(String date){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.parse(date));
    }
}
