package com.xss.mobile.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by wangzhongcai on 2015/11/12.
 */
public class StringUtil {

    /**
     * 获取提示数内容 大于99显示99+
     *
     * @param count 提示数
     * @return 提示数内容
     */
    public static String getTipsCount(int count) {
        String countStr = count + "";
        if (count > 98) {
            countStr = "99+";
        }
        return countStr;
    }

    /**
     * 获取提示数内容 大于98显示99+
     *
     * @param count 提示数
     * @return 提示数内容
     */
    public static String getTipsCount98(int count) {
        String countStr = count + "";
        if (count > 98) {
            countStr = "99+";
        }
        return countStr;
    }

    public static String to2AndDelEndOfZero(double x) {
        // DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat df = new DecimalFormat("0.00");
        String yy = df.format(x);
        if (yy.indexOf(".") > 0) {
            yy = yy.replaceAll("0+?$", "");// 去掉多余的0
            yy = yy.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return yy;
    }

    public static boolean isStringGreaterThanZero(String yy) {
        if (!TextUtils.isEmpty(yy)) {
            if (yy.length() > 0 && !to2AndDelEndOfZero(yy).equals("0")) {
                return true;
            }
        }
        return false;
    }

    public static String to2AndDelEndOfZero(String yy) {
        if (TextUtils.isEmpty(yy)) {
            return "0";
        }
        if (yy.contains(".")) {
            if (yy.indexOf(".") > 0) {
                yy = yy.replaceAll("0+?$", "");// 去掉多余的0
                yy = yy.replaceAll("[.]$", "");// 如最后一位是.则去掉
            }
        }
        return yy;
    }

    public static String stringToMoney(String yy) {
        if (TextUtils.isEmpty(yy)) {
            return "0";
        }

        double d = Double.valueOf(to2AndDelEndOfZero(yy));

        NumberFormat nf = new DecimalFormat("#,###.##");
        return nf.format(d);
    }

    public static String stringToMoney(double d) {
        NumberFormat nf = new DecimalFormat("#,###.##");
        return nf.format(d);
    }

    public static String toFilterPhoneEmpty(String phone) {
        if (phone != null) {
            phone = phone.replaceAll("\\s", "");
            phone = phone.replace("-", "");
        }
        return phone;
    }
}
