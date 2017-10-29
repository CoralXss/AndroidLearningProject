package com.xss.mobile.activity.eventbus;

import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xss.mobile.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventbusTestActivity extends AppCompatActivity {
    private TextView tv_msg;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus_test);

//        EventBus.getDefault().register(this);

        initView();

    }

    private void initView() {
        tv_msg = (TextView) findViewById(R.id.tv_msg);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                EventBus.getDefault().postSticky(new MessageEvent("index = " + index, ""));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        print();
                    }
                }).start();

            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().register(EventbusTestActivity.this);
            }
        });

        findViewById(R.id.btn_unregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().unregister(EventbusTestActivity.this);
            }
        });
    }

    private void print() {
        Log.e("OnClickPost", Thread.currentThread().getName());
        EventBus.getDefault().post(new MessageEvent("hello", "world"));

    }

    private boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * PostThread：事件从哪个线程发布就由哪个线程接收处理
     *      [若事件由 UI线程发出，可以在此方法中更新 UI；若由非 UI线程发出，则禁止 更新UI]
     *      (事件处理方法中避免耗时操作以引起ANR)
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(MessageEvent message) {
        if (isMainThread()) {
            tv_msg.setText(message.name + ", " + message.password);
        }
        Log.e("PostThread", Thread.currentThread().getName() + ", msg = " + message.name);
    }

    /**
     * MainThread：不论事件是从哪个线程发出，事件处理函数都会在 UI线程中执行
     *      (事件处理方法可以用来更新 UI，但是不能处理耗时操作)
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 0)
    public void onMessageEventMainThread(MessageEvent message) {
        tv_msg.setText(message.name + ", " + message.password);
        Log.e("MainThread", Thread.currentThread().getName() + ", msg = " + message.name);
    }

    /**
     * Background：若事件是从 UI线程中 发布出来的，则事件处理函数就会在新的线程中执行；
     *              若事件本就是从子线程发布出来的，则直接在发布事件的线程中执行。
     *         (事件处理函数中禁止 UI更新操作)
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    public void onMessageEventBackgroundThread(MessageEvent message) {
//        tv_msg.setText(message.name + ", " + message.password);
        Log.e("BackgroundThread", Thread.currentThread().getName() + ", msg = " + message.name);
    }

    /**
     * Async：不论事件是从哪个线程发布出来的，该事件处理函数都会在新建的子线程中执行
     *      (事件处理函数中禁止 UI更新操作)
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    public void onMessageEventAsync(MessageEvent message) {
//        tv_msg.setText(message.name + ", " + message.password);
        Log.e("Async", Thread.currentThread().getName() + ", msg = " + message.name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class MessageEvent {
        public String name;
        public String password;

        public MessageEvent(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }
}
