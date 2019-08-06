package com.xss.mobile.activity.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by xss on 2017/11/3.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private String TAG = AlarmBroadcastReceiver.class.getSimpleName();

    private void log(String msg) {
        Log.e(TAG, msg);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        log("---received message---");

    }

}
