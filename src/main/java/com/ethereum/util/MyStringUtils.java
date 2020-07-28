package com.ethereum.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: Malakh
 * @Date: 2020/2/11
 * @Description: 字符串工具类
 */
public class MyStringUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyStringUtils.class);
    private static final SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 生成随机UUID
     *
     * @return
     */
    public static String buildUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 检查日期是否符合 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param str
     * @return
     */
    public static boolean checkDateStrFormat(String str) {
        if (StringUtils.isEmpty(str)) {
            return true;
        }
        return str.matches("[12][0-9]{3}-[01][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]");
    }

    public static String parseDateToStr(Date date) {
        if (date != null) {
            return sm.format(date);
        }
        return null;
    }

    public static Date parseStrToDate(String str) {
        Preconditions.checkArgument(checkDateStrFormat(str), "时间格式%s 不合法!", str);
        try {
            return sm.parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException("时间格式" + str + "不合法");
        }
    }

    /**
     * 时间date加上 n 分钟/小时 后的时间
     *
     * @param number
     * @param unit
     * @return
     */
    public static Date plusDate(Date date, Integer number, String unit) {
        if (number == null || number <= 0) {
            LOGGER.error("[plusDate] number:{} illegal.");
            return new Date();
        }
        if (StringUtils.isEmpty(unit)) {
            LOGGER.error("[plusDate] unit is empty.");
            return new Date();
        }

        DateTime dateTime = new DateTime(date);
        unit = unit.toLowerCase();
        switch (unit) {
            case "second":
                dateTime = dateTime.plusSeconds(number);
                break;
            case "minute":
                dateTime = dateTime.plusMinutes(number);
                break;
            case "hour":
                dateTime = dateTime.plusHours(number);
                break;
            case "day":
                dateTime = dateTime.plusDays(number);
                break;
            case "month":
                dateTime = dateTime.plusMonths(number);
                break;
            case "year":
                dateTime = dateTime.plusYears(number);
                break;
            default:
                LOGGER.error("unit:{} illegal", unit);
        }

        return dateTime.toDate();
    }

    /**
     * 获取时间date减去 n 分钟/小时 后的时间
     *
     * @param number
     * @param unit
     * @return
     */
    public static Date minusDate(Date date, Integer number, String unit) {
        if (number == null || number <= 0) {
            LOGGER.error("[minusDate] number:{} illegal.");
            return new Date();
        }
        if (StringUtils.isEmpty(unit)) {
            LOGGER.error("[minusDate] unit is empty.");
            return new Date();
        }

        DateTime dateTime = new DateTime(date);
        unit = unit.toLowerCase();
        switch (unit) {
            case "second":
                dateTime = dateTime.minusSeconds(number);
                break;
            case "minute":
                dateTime = dateTime.minusMinutes(number);
                break;
            case "hour":
                dateTime = dateTime.minusHours(number);
                break;
            case "day":
                dateTime = dateTime.minusDays(number);
                break;
            case "month":
                dateTime = dateTime.minusMonths(number);
                break;
            case "year":
                dateTime = dateTime.minusYears(number);
                break;
            default:
                LOGGER.error("unit:{} illegal", unit);
        }

        return dateTime.toDate();
    }

    /**
     * 查看时间 timeStr 是否在当前时间 n 分钟/小时 之内
     *
     * @param dateStr
     * @param timeNumber
     * @param unit
     * @return
     */
    public static boolean checkDateIsBetweenNow(String dateStr, Integer timeNumber, String unit) {
        if (StringUtils.isEmpty(dateStr)) {
            return false;
        }
        try {
            Date parseDate = sm.parse(dateStr);

            Date minDate = minusDate(new Date(), timeNumber, unit);
            Date maxDate = plusDate(new Date(), timeNumber, unit);

            return checkAIsAfterB(parseDate, minDate) && checkAIsBeforeB(parseDate, maxDate);
        } catch (ParseException e) {
            LOGGER.error("[] Exception", e);
            return false;
        }
    }

    /**
     * 判断时间A 是否早于时间B
     *
     * @param timeStrA
     * @param timeStrB
     * @return
     */
    public static boolean checkAIsBeforeB(String timeStrA, String timeStrB) {
        try {
            Date dateA = sm.parse(timeStrA);
            Date dateB = sm.parse(timeStrB);

            return dateA.before(dateB);
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.error("[checkIsBefore] Exception", e);
            return false;
        }
    }

    public static boolean checkAIsBeforeB(Date dateA, Date dateB) {
        return dateA.before(dateB);
    }

    /**
     * 判断时间A 是否晚于时间B
     *
     * @param timeStrA
     * @param timeStrB
     * @return
     */
    public static boolean checkAIsAfterB(String timeStrA, String timeStrB) {
        try {
            Date dateA = sm.parse(timeStrA);
            Date dateB = sm.parse(timeStrB);

            return dateA.after(dateB);
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.error("[checkAIsAfterB] Exception", e);
            return false;
        }
    }

    public static boolean checkAIsAfterB(Date dateA, Date dateB) {
        return dateA.after(dateB);
    }
}
