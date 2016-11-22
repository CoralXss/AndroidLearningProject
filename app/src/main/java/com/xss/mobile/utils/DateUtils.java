package com.xss.mobile.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 负责日期相关的功能.
 *
 * @author John
 */
public final class DateUtils {

    // 刚刚,1分钟前,3分钟前,5分钟前,30分钟前,1小时前,2小时前,5小时前,12小时前,1天前,2天前,1周前,1个月前
    public static final long MINUTE_1 = 60 * 1000;
    public static final long MINUTE_3 = MINUTE_1 * 3;
    public static final long MINUTE_5 = MINUTE_1 * 5;
    public static final long MINUTE_30 = MINUTE_1 * 30;
    public static final long HOUR_1 = MINUTE_1 * 60;
    public static final long HOUR_2 = HOUR_1 * 2;
    public static final long HOUR_5 = HOUR_1 * 5;
    public static final long HOUR_12 = HOUR_1 * 12;
    public static final long DAY_1 = HOUR_1 * 24;
    public static final long DAY_2 = DAY_1 * 2;
    public static final long WEEK_1 = DAY_1 * 7;
    public static final long MONTH_1 = DAY_1 * 30;
    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_A = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_7 = "HH:mm";
    public static final String FORMAT_9 = "MM-dd HH:mm";
    public static final String FORMAT_B = "MM-dd";
    private static final int DAYS_IN_A_WEEK = 7;
    private static final int MAX_DAYS_IN_A_MONTH = 31;
    private static final int MAX_DAYS_IN_A_YEAR = 366;

    @SuppressLint("SimpleDateFormat")
    public static String formatDateToString(long milliseconds, String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        Date d = new Date(milliseconds);
        String s = format.format(d);
        return s;
    }

    public static String getChatDatetimeDesc(long time) {
        Calendar curCal = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Date date = cal.getTime();
        if (curCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            if (curCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    && curCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {// 今天
                return dateToString(date, FORMAT_7);
            }

            curCal.add(Calendar.DATE, -1);// 昨天
            if (curCal.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) {
                return "昨天 " + dateToString(date, FORMAT_7);
            }

            return dateToString(date, FORMAT_9);
        } else {
            return dateToString(date, FORMAT_A);
        }
    }

    public static String getBaseChatDatetimeDesc(long time) {
        Calendar curCal = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Date date = cal.getTime();
        if (curCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            if (curCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    && curCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {// 今天
                return dateToString(date, FORMAT_7);
            }

            curCal.add(Calendar.DATE, -1);// 昨天
            if (curCal.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) {
                return "昨天";
            }

            return dateToString(date, FORMAT_B);
        } else {
            return dateToString(date, FORMAT_A);
        }
    }

    public static String dateToString(Date data, String format) {
        format = (format == null || format.length() <= 0) ? FORMAT_1 : format;
        return new SimpleDateFormat(format).format(data);
    }

    public static long dateTimeToStamp(String timeStr) {

        if (TextUtils.isEmpty(timeStr)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        try {
            return sdf.parse(timeStr.replace("Z", " UTC")).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将数据库字符串转换成日期类.发生异常则返回空.
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date converToDate(String sqlDateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date parse = simpleDateFormat.parse(sqlDateStr);
            return parse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String converToNewDate(String sqlDateStr, String format) {
        try {
            SimpleDateFormat simpleDateFormat = null;
            if (sqlDateStr != null) {
                if (sqlDateStr.length() == 16) {
                    simpleDateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm");
                } else {
                    simpleDateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                }
            } else {
                simpleDateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
            }
            Date parse = simpleDateFormat.parse(sqlDateStr);
            return dateToStr(parse, format);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String converToNYR(String sqlDateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");
            Date parse = simpleDateFormat.parse(sqlDateStr);
            return dateToShareStr(parse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToShareStr(Date dateDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(dateDate);
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取抢客的开始时间格式 <功能详细描述>
     *
     * @param dateDate
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateToStr(Date dateDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日HH:mm");
            String dateString = formatter.format(dateDate);
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String dataToStr3(Date date) {

        SimpleDateFormat formatter = null;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
        if (c2.getTimeInMillis() < date.getTime()) {
            formatter = new SimpleDateFormat("HH:mm");
        } else {
            formatter = new SimpleDateFormat("yyyy/MM/dd");
        }
        return formatter.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToStr(Date dateDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String getRelativeDateTimeString(long timestamp) {
        Date date = new Date();
        long diff = date.getTime() - timestamp;
        if (diff <= MINUTE_1) {
            return "刚刚";
        }
        if (diff > MINUTE_1 && diff <= MINUTE_3) {
            return "1分钟前";
        }
        if (diff > MINUTE_3 && diff <= MINUTE_5) {
            return "3分钟前";
        }
        if (diff > MINUTE_5 && diff <= MINUTE_30) {
            return "5分钟前";
        }
        if (diff > MINUTE_30 && diff <= HOUR_1) {
            return "30分钟前";
        }
        if (diff > HOUR_1 && diff <= HOUR_2) {
            return "1小时前";
        }
        if (diff > HOUR_2 && diff <= HOUR_5) {
            return "2小时前";
        }
        if (diff > HOUR_5 && diff <= HOUR_12) {
            return "5小时前";
        }
        if (diff > HOUR_12 && diff <= DAY_1) {
            return "12小时前";
        }
        if (diff > DAY_1 && diff <= DAY_2) {
            return "1天前";
        }
        if (diff > DAY_2 && diff <= WEEK_1) {
            return "2天前";
        }
        if (diff > WEEK_1 && diff <= MONTH_1) {
            return "1周前";
        }
        if (diff > MONTH_1) {
            return "1个月前";
        }
        return "很久以前";
    }

    public static final SimpleDateFormat dateFormatYearMonth = new SimpleDateFormat(
            "yyyy年MM月", Locale.CHINESE);
    public static String getRelativeDateTimeDesc(long when) {
        long currentTimeMillis = System.currentTimeMillis();
        if (when >= currentTimeMillis) {
            return "今天";
        }
        if (DateUtils.isToday(when)) {
            return "今天";
        }
        if (DateUtils.isYesterday(when)) {
            return "昨天";
        }
        if (DateUtils.isDayBeforYesterday(when)) {
            return "前天";
        }
        if (DateUtils.isThisWeek(when)) {
            return "本周";
        }
        if (DateUtils.isThisMonth(when)) {
            return "本月";
        }
        if (DateUtils.isThisYear(when)) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(when);
            return (instance.get(Calendar.MONTH) + 1) + "月";
        }
        return dateFormatYearMonth.format(new Date(when));
    }

    /**
     * 判断是否在n天之内.
     *
     * @param when
     * @return
     */
    public static boolean isInDays(long when, int days) {
        long diff = DAY_1 * days;
        long tmpTime = System.currentTimeMillis() - diff;
        return tmpTime <= when;
    }

    public static boolean isSameDay(long dayOne, long dayTwo) {
        Time time = new Time();
        time.set(dayOne);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(dayTwo);
        return (thenYear == time.year) && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    /**
     * 是否本月.
     *
     * @param when
     * @return
     */
    public static boolean isThisMonth(long when) {
        return isThisTimeFrame(when, Calendar.MONTH);
    }

    private static boolean isThisTimeFrame(long when, int field) {
        int days = 0;
        if (field == Calendar.WEEK_OF_MONTH) {
            days = DAYS_IN_A_WEEK;
        } else if (field == Calendar.MONTH) {
            days = MAX_DAYS_IN_A_MONTH;
        } else if (field == Calendar.YEAR) {
            days = MAX_DAYS_IN_A_YEAR;
        }
        if (isInDays(when, days)) {
            Calendar instance = Calendar.getInstance();
            int now = instance.get(field);
            instance.setTimeInMillis(when);
            int date = instance.get(field);
            if (now == date) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否本周.
     *
     * @param when
     * @return
     */
    public static boolean isThisWeek(long when) {
        return isThisTimeFrame(when, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 是否本年.
     *
     * @param when
     * @return
     */
    public static boolean isThisYear(long when) {
        return isThisTimeFrame(when, Calendar.YEAR);
    }

    public static boolean isToday(long when) {
        return isSameDay(when, System.currentTimeMillis());
    }

    public static boolean isYesterday(long when) {
        long newDay = when + DAY_1;
        return isSameDay(newDay, System.currentTimeMillis());
    }

    public static boolean isDayBeforYesterday(long when) {
        long newDay = when + DAY_2;
        return isSameDay(newDay, System.currentTimeMillis());
    }

    /**
     * 将日期转换成字符串.
     *
     * @param date
     * @param fommat
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String toString(Date date, String fommat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fommat);
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * {}
     */
    public static Calendar getCalendar(String date) {
        Calendar cal = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date dat = format.parse(date);
            cal = Calendar.getInstance();
            cal.setTime(dat);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return cal;
    }


    public static long getTimer(int year, int mouth, int day, int hour, int mis, int sec) throws Exception {
        String timeStr = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, mouth, day, hour, mis, sec);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(timeStr).getTime();
    }

    public static long getTimer2(int year, int mouth) throws Exception {
        String timeStr = String.format("%04d-%02d-00 00:00:00", year, mouth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(timeStr).getTime();
    }


    public static String formatTime(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return String.format("%d月%d日", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    public static String formatTime2(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date dat = format.parse(dateStr);
            return toString(dat, "yyyy年MM月");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    public static String formatTime3(String dateStr) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dat = format.parse(dateStr);
            Calendar tar = Calendar.getInstance();
            tar.setTime(dat);
            Calendar cur = Calendar.getInstance();
            int year = cur.get(Calendar.YEAR) - tar.get(Calendar.YEAR);
            int month = (cur.get(Calendar.MONTH) + 1) - (tar.get(Calendar.MONTH) + 1);
            year = year < 1 ? 1 : month < 0 ? year : year + 1;
            return String.format("%d年", year);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    public static boolean checkAfterCurrentDate(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dat = format.parse(dateStr);
            Calendar tar = Calendar.getInstance();
            tar.setTime(dat);
            Calendar cur = Calendar.getInstance();
            return Calendar.getInstance().after(tar);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return false;
    }

    public static String formatDate(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dat = format.parse(dateStr);
            Calendar tar = Calendar.getInstance();
            tar.setTime(dat);
            Calendar cur = Calendar.getInstance();
            if (Calendar.getInstance().after(tar)) {
                return dateStr;
            } else {
                return String.format("%4d-%2d-01 00:00:00", cur.get(Calendar.YEAR),
                        cur.get(Calendar.MONTH) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return dateStr;
    }

    public static long dateTimeToStamp2(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMessageDatetimeDesc(String dateTime) {
        long time = dateTimeToStamp2(dateTime);

        Calendar curCal = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Date date = cal.getTime();

        if (curCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            if (curCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    && curCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {// 今天
                return dateToString(date, FORMAT_7);
            }

            return formatTime5(dateTime);
        } else {
            return formatTime5(dateTime);
        }
    }

    public static String formatTime5(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dat = format.parse(dateStr);
            return toString(dat, "yyyy/MM/dd");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return "";
    }
}
