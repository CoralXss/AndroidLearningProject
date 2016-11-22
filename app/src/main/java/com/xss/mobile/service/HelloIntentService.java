package com.xss.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by xss on 2016/10/10.
 */
public class HelloIntentService extends IntentService {
    String TAG = "HelloIntentService";

    public HelloIntentService() {
        super("HelloIntentService");  // name for the worker thread
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long endTime = System.currentTimeMillis() + 5 * 1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    long time = endTime - System.currentTimeMillis();
                    Log.e(TAG, "handle wait" + time);
                    wait(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
