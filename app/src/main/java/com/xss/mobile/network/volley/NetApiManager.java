package com.xss.mobile.network.volley;

import android.content.Context;
import android.service.voice.VoiceInteractionSession;

import com.fangdd.android.volley.Request;

/**
 * Created by xss on 2016/11/18.
 * desc: 业务逻辑请求接口
 */
public class NetApiManager {

    private Context mContext = null;

    private static NetApiManager mInstance;

    private NetApiManager(Context mContext) {
        this.mContext = mContext;
    }

    public static NetApiManager getInstance(Context mContext) {
        if (null == mInstance) {
            synchronized (NetApiManager.class) {
                if (null == mInstance) {
                    mInstance = new NetApiManager(mContext.getApplicationContext());
                }
            }
        }
        return mInstance;
    }


    public void testGet(String name, RequestListener listener) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        RequestManager.getInstance(mContext).sendRequest(Request.Method.GET, "http://www.baidu.com", null, params, true, listener);
    }

    public void testPost(String name, RequestListener listener) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        RequestManager.getInstance(mContext).sendRequest(Request.Method.POST, "http://www.baidu.com", null, params, true, listener);
    }
}
