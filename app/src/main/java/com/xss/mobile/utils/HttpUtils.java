package com.xss.mobile.utils;

/**
 * Created by xss on 2017/3/8.
 */

public class HttpUtils {

    private static HttpUtils mInstance;

    private HttpUtils() {}

    public static HttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }
            }
        }
        return mInstance;
    }



}
