package com.xss.mobile.activity.broadcast;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.xss.mobile.MainActivity;
import com.xss.mobile.R;

/**
 * Created by xss on 2017/4/13.
 */

public class MyService extends Service {
    String TAG = MyService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 在前台显示 Service
        setForeground();


        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setForeground() {
        Notification.Builder nb = new Notification.Builder(getApplicationContext());

        Intent intent = new Intent(this, MainActivity.class);
        nb.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.add))
                .setContentTitle("This is title")
                .setContentText("text text text")
                .setWhen(System.currentTimeMillis());  // 设置通知显示的时间

        Notification n = nb.build();
        n.defaults = Notification.DEFAULT_SOUND;

        // 当使用的通知ID一致时，只会更新当前Notification
        startForeground(110, n);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stopForeground(true);  // 停止前台服务
        super.onDestroy();
    }
}
