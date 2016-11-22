package com.xss.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xss on 2016/10/10.
 * desc: 用Service和 HandlerThred 模拟 IntentService用法
 *
 * IntentService特点：
 * 1）创建一个独立的工作线程来处理所有通过 onStartCommand()传递给服务的Intents;
 * 2）创建一个工作队列，来逐个发送intent 给 onHandleIntent();
 * 3）不需要主动调用 stopSelf() 来结束服务，在所有的 Intent处理完毕后，系统会自动关闭服务；
 * 4）默认实现的 onBind() 返回null;
 * 5）默认实现的 onStartCommand()的目的时将 Intent插入到工作队列中。
 *
 * IntentService和Service的区别是： IntentService在执行onCreate时内部开了一个线程去执行你的耗时操作。
 */
public class HelloService extends Service {
    String TAG = "HelloService";

    private Looper mServiceLopper;
    private ServiceHandler serviceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            long endTime = System.currentTimeMillis() + 5 * 1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        long time = endTime - System.currentTimeMillis();
                        Log.e(TAG, "handle wait" + time + ", " + msg.arg1);
                        wait(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 通过startId来关闭Service,可以避免停止其他正在处理工作的Service
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // onCreate只会调用一次，并且只在第一次创建Service时调用
        Log.e(TAG, "Service onCreate");

        HandlerThread handlerThread = new HandlerThread("serviceStartArgs",
                Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();

        mServiceLopper = handlerThread.getLooper();
        serviceHandler = new ServiceHandler(mServiceLopper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 多次启动Service,其实例只存在一个，但是startId是递增的
        Log.e(TAG, "Service starting " + startId);

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service destroy");
    }
}
