package com.xss.mobile.activity.broadcast;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by xss on 2017/4/12.
 */

public class MyHandlerThread extends Thread {
    private static final String TAG = MyHandlerThread.class.getSimpleName();

    private MyServiceHandler mChildThreadHandler;
    private Handler mMainThreadHandler;

    private Looper myLooper;

    public MyHandlerThread(String data) {
        super(data);

        mChildThreadHandler = null;
    }

    /**
     * 主线程传入自己的 Handler，方便子线程传递消息(在主线程执行耗时操作，会 dropped frames 丢帧)
     * @param handler
     */
    public void setMainHandler(Handler handler) {
        this.mMainThreadHandler = handler;
    }

    /**
     * 在主线程获取，将主线程的消息发给子线程
     * @return
     */
    public Handler getHandler() {
        return mChildThreadHandler;
    }

    private final class MyServiceHandler extends Handler {

        public MyServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what = 0x11) {
                case 0x11:
                    Log.e(TAG, Thread.currentThread().getName());
                    Log.e(TAG, "Msg received from main handler thread: broadcast send = " + msg.obj.toString());

                    // 模拟耗时请求
                    SystemClock.sleep(3000);
                    Log.e(TAG, "Is Broadcast ANR ?");

                    // 子线程处理消息后，给主线程发消息，说"我执行完了"
                    Message mainMsg = new Message();
                    mainMsg.what = 0x22;
                    mainMsg.obj = "Msg from MyHandlerThread: I have done !";
                    mMainThreadHandler.sendMessage(mainMsg);

                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void run() {
        Looper.prepare();

        synchronized (this) {
            // 获取本线程 Looper 对象
            myLooper = Looper.myLooper();
        }

        // 在 run 方法中创建 Handler 才会是 子线程的 Handler，如果在其他地方创建 对应的是主线程或者其他线程
        // 这一步根据 Looper 直接绑定了 Handler 和 Thread
        mChildThreadHandler = new MyServiceHandler(myLooper);

        Looper.loop();
    }
}
