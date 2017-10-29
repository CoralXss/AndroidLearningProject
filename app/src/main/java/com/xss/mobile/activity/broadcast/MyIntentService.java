package com.xss.mobile.activity.broadcast;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xss on 2017/4/13.
 */

public class MyIntentService extends IntentService {
    private static final String TAG = MyIntentService.class.getSimpleName();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // "com.xss.mobile.Test"
            Log.e(TAG, "inline action = " + intent.getAction());


        }
    };

    public MyIntentService() {
        this("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "IntentService is created");

        registerReceiver(receiver, new IntentFilter("com.xss.mobile.Test"));
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "IntentService is started");

        return super.onStartCommand(intent, flags, startId);
    }

    // 该方法是在 HandlerThread异步消息处理线程的消息队列中取出消息进行处理，处理完后，IntentService 就会调用 stopSelf 方法停止
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "IntentService is handling intent msg");

        if ("com.xss.startIntentService".equals(intent.getAction())) {
            String msg = intent.getStringExtra("data");

            Log.e(TAG, Thread.currentThread().getName());
            Log.e(TAG, "Msg received from main handler thread: broadcast send = " + msg);
            // 处理耗时请求
            SystemClock.sleep(3000);
            Log.e(TAG, "Is Broadcast ANR ?");

            // 子线程处理消息后，给主线程发消息，说"我执行完了"
            Message mainMsg = new Message();
            mainMsg.what = 0x22;
            mainMsg.obj = "Msg from child handler thread: I have done !";

            // 发送广播？？？
            Intent intent0 = new Intent(BroadcastTestActivity.ACTION_FROM_INTENTSERVICE);
            intent0.putExtra("data_from_intent_service", "Msg from MyIntentService: I have done !");
            sendBroadcast(intent0);

            // 如果消息队列为空，就会直接结束Service，接着执行onDestroyed方法

            Intent intent1 = new Intent("com.xss.mobile.Test");
            intent1.putExtra("data_test", "Msg from MyIntentService: I have done !");
            sendBroadcast(intent0);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "IntentService is destroyed");
        unregisterReceiver(receiver);
    }
}
