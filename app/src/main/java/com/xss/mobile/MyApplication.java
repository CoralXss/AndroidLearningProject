package com.xss.mobile;

import android.app.Application;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import com.fangdd.android.volley.*;
import com.fangdd.android.volley.BuildConfig;
import com.fangdd.android.volley.toolbox.HttpHeaderParser;
import com.xss.mobile.network.okhttp.OkHttpManager;
import com.xss.mobile.service.multiprocess.ServiceA;
import com.xss.mobile.service.multiprocess.ServiceB;

import org.xutils.x;

import java.io.IOException;

/**
 * Created by xss on 2016/11/29.
 */
public class MyApplication extends Application {
    private static final String TAg = MyApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAg, "on process id = " + Process.myPid());

        startService(new Intent(this, ServiceA.class));
        startService(new Intent(this, ServiceB.class));

        init();
    }

    private void init() {
        // 初始化 xutils
        x.Ext.init(this);
        // 是否打印日志，开启DEBUG会影响性能
        x.Ext.setDebug(BuildConfig.DEBUG);

        try {
            // 设置自签名证书
            Log.d(TAg, "app init");
            OkHttpManager.getInstance().setCertificates(getAssets().open("srca.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
