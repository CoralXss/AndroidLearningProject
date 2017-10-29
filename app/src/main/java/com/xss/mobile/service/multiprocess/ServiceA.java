package com.xss.mobile.service.multiprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xss on 2017/4/19.
 *
 * 注： ServiceA 中的日志打印需要选中 ServiceA所在进程后才能看到打印的日志，可以看到创建了两个进程，pid不同
 */

public class ServiceA extends Service {
    private static final String TAG = ServiceA.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "ServiceA onCreate on pid = " + Process.myPid());
    }

    // @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "ServiceA onStart on pid " + Process.myPid());

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "ServiceA onDestroy on pid " + Process.myPid());

        Intent intent = new Intent("com.xss.mobile.RESTART_SERVICE");
        sendBroadcast(intent);

        super.onDestroy();
    }
}
