package com.xss.mobile.network.volley;

import com.fangdd.android.volley.Request;

import org.json.JSONObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xss on 2016/11/18.
 */
public class RequestParams {
    private static final String TAG = Request.class.getSimpleName();

    private static final String ENCODING = "utf-8";

    /** ConcurrentHashMap将Map分成N个Segment（类似HashTable），提供线程安全，但效率默认提高16倍 **/
    private ConcurrentHashMap<String, String> paramsMap = null;

    private ConcurrentHashMap<String, String> headersMap = null;

    /**
     * 传输字符串参数
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if (paramsMap == null) {
            paramsMap = new ConcurrentHashMap<>();
        }
        if (null != key && null != value) {
            paramsMap.put(key, value);
        }
    }

    /**
     * 拼接请求参数
     * 格式： url?body={json}
     * @return
     */
    public String getBody() {
        StringBuffer sb = new StringBuffer("?body=");
        if (paramsMap != null) {
            JSONObject json = new JSONObject(paramsMap);
            sb.append(json.toString());
        }
        return sb.toString();
    }

    /**
     * 获取header头部
     * @return
     */
    public ConcurrentHashMap<String, String> getHeader() {
        if (headersMap == null) {
            headersMap = new ConcurrentHashMap<>();
        }
        headersMap.put("platform", "Android");
        headersMap.put("appVersion", "7.6.2");

        return headersMap;
    }


}
