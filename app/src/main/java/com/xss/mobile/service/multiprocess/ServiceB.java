package com.xss.mobile.service.multiprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xss on 2017/4/19.
 */

public class ServiceB extends Service {
    private static final String TAG = ServiceB.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.e(TAG, "ServiceB onCreate on pid " + Process.myPid());
        super.onCreate();
    }

    // @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "ServiceB onStart on pid " + Process.myPid());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "ServiceB onDestroy on pid " + Process.myPid());

        super.onDestroy();
    }
}
