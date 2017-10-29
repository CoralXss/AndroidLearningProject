package com.xss.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.xss.mobile.service.multiprocess.ServiceA;

/**
 * Created by xss on 2017/4/19.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = TextUtils.isEmpty(intent.getAction()) ? "action is empty" : intent.getAction();

        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
        if ("com.xss.mobile.RESTART_SERVICE".equals(action)) {
            Intent i = new Intent(context, ServiceA.class);
            context.startService(i);
        }
    }
}
