package com.xss.mobile;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import com.xss.mobile.activity.dagger2.DaggerComponent;
import com.xss.mobile.activity.dagger2.DaggerDaggerComponent;
import com.xss.mobile.activity.dagger2.DaggerModule;
import com.xss.mobile.network.okhttp.OkHttpManager;
import com.xss.mobile.service.multiprocess.ServiceA;
import com.xss.mobile.service.multiprocess.ServiceB;

import java.io.IOException;
import java.util.List;

/**
 * Created by xss on 2016/11/29.
 */
public class MyApplication extends Application {
    private static final String TAg = MyApplication.class.getSimpleName();

    private DaggerComponent daggerComponent;

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    public DaggerComponent getDaggerComponent() {
        return daggerComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        daggerComponent = DaggerDaggerComponent.builder()
                .daggerModule(new DaggerModule())
                .build();

        // 注：在 Application 中初始化时，需要注意多进程的情况，所以要判断是否是当前进程
        if (isInMainProcess(this)) {
            init();
        }
        Log.e(TAg, "on process id = " + Process.myPid());

        startService(new Intent(this, ServiceA.class));
        startService(new Intent(this, ServiceB.class));

        init();
    }

    private void init() {
        // 初始化 xutils
//        x.Ext.init(this);
//        // 是否打印日志，开启DEBUG会影响性能
//        x.Ext.setDebug(BuildConfig.DEBUG);

        try {
            // 设置自签名证书
            Log.d(TAg, "app init");
            OkHttpManager.getInstance().setCertificates(getAssets().open("srca.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isInMainProcess(Context context) {
        return BuildConfig.APPLICATION_ID.equals(getCurrentProcessName(context));
    }

    public String getCurrentProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
        if (processList != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : processList) {
                if (processInfo.pid == android.os.Process.myPid()) {
                    return processInfo.processName;
                }
            }
        }
        return BuildConfig.APPLICATION_ID;
    }
}
