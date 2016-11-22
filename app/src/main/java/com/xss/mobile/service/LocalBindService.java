package com.xss.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by xss on 2016/10/11.
 */
public class LocalBindService extends Service {
    String TAg = "LocalBindService";

    // 给客户端的Binder
    private final IBinder mBinder = new LocalBinder();
    // 生成随机数
    private final Random random = new Random();

    /**
     * 用于客户端Binder的类：
     * 因为知道本服务总是运行于客户端相同的进程中，就不需要利用IPC进行处理
     */
    public class LocalBinder extends Binder {

        public LocalBindService getService() {
            return LocalBindService.this;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAg, "onCreate");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAg, "onBind");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAg, "onDestroy");
    }

    public int getRandomNumber() {
        return random.nextInt(100);
    }
}
