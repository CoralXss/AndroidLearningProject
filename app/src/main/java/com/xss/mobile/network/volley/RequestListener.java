package com.xss.mobile.network.volley;

/**
 * Created by xss on 2016/11/18.
 */
public interface RequestListener {
    /**
     * 请求开始
     */
    void onPreExecute();

    /**
     * 请求成功
     * @param data
     */
    void onSuccess(String data);

    /**
     * 请求失败
     * @param code
     * @param msg
     */
    void onFail(String code, String msg);

    /**
     * 请求结束
     */
    void onFinish();
}
