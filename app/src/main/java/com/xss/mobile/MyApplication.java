package com.xss.mobile;

import android.app.Application;
import android.util.Log;

import com.fangdd.android.volley.toolbox.HttpHeaderParser;
import com.xss.mobile.network.okhttp.OkHttpManager;

import java.io.IOException;

/**
 * Created by xss on 2016/11/29.
 */
public class MyApplication extends Application {
    private static final String TAg = MyApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        try {
            // 设置自签名证书
            Log.d(TAg, "app init");
            OkHttpManager.getInstance().setCertificates(getAssets().open("srca.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
