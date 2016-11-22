package com.xss.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xss on 2016/10/12.
 */
public class LocalStartService extends Service {
    String TAg = "LocalStartService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAg, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAg, "onStartCommand");

        if (null != intent) {
            Log.e(TAg, "onStartCommand " + startId + ", " + intent);
        } else {
            Log.e(TAg, "onStartCommand " + startId + ", intent = null");
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAg, "onDestroy");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
