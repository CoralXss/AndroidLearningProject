package com.xss.mobile.activity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xss.mobile.R;

public class BroadcastTestActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainThread";

    public static final String ACTION_SEND_MSG_BY_MYHANDLERTHREAD = "com.xss.download.by.myHandlerThread";
    public static final String ACTION_SEND_MSG_BY_MYINTENTSERVICE = "com.xss.download.by.myIntentService";
    public static final String ACTION_FROM_INTENTSERVICE = "com.xss.download.intentService";

    private Button btn_send_my_handler_thread, btn_send_intent_service;
    private TextView tv_msg, tv_msg_handler_thread, tv_msg_intent_service;

    private DownloadBroadcastReceiver downloadBroadcastReceiver;

    // 执行耗时操作
    private MyHandlerThread handlerThread;

    // 接收 MyHandlerThread 异步消息线程的回调结果
    private MainHandler mainHandler;

    private final class MainHandler extends Handler {

        public MainHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x22:
                    // 方式一回调结果：也可以使用广播
                    tv_msg_handler_thread.append("\n");
                    tv_msg_handler_thread.append("--onReceiver: received data from MyHandlerThread !");
                    tv_msg_handler_thread.append("\n");
                    tv_msg_handler_thread.append("--data = " + msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }

    // 使用本地广播 LocalBroadcast 会更好些
    class DownloadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Skipped 5999 frames!  目前只提示丢帧，没有出现 ANR 弹窗
//            SystemClock.sleep(100000);

            tv_msg.append("--onReceive: action = " + intent.getAction());
            tv_msg.append("\n");
            String data = intent.getStringExtra("data");

            // 3. 执行耗时操作
            if (ACTION_SEND_MSG_BY_MYHANDLERTHREAD.equals(intent.getAction())) {
                tv_msg_handler_thread.append("--onReceiver: process time-consuming by myHandlerThread !");
                tv_msg_handler_thread.append("\n");

                // 方式一：自定义异步消息线程处理耗时操作，并且通过主线程的 Handler / 广播 回传处理成功的回调
                sendMsgByDefineHandlerThread(data);

            } else if (ACTION_SEND_MSG_BY_MYINTENTSERVICE.equals(intent.getAction())) {
                tv_msg_intent_service.append("--onReceiver: process time-consuming by IntentService !");
                tv_msg_intent_service.append("\n");

                // 方式二：使用 IntentService 处理耗时操作，并且通过广播回传处理成功的回调
                sendMsgByIntentService(context, data);

            } else if (ACTION_FROM_INTENTSERVICE.equals(intent.getAction())) {

                // 方式二回调结果：Service通过广播发送数据，更新UI
                tv_msg_intent_service.append("\n");
                tv_msg_intent_service.append("--onReceiver: received data from IntentService !");
                tv_msg_intent_service.append("\n");
                tv_msg_intent_service.append("--data = " + intent.getStringExtra("data_from_intent_service"));
                tv_msg_intent_service.append("\n");

            }
        }
    }

    /**
     * 在onReceiver 方法中执行耗时操作，
     * 策略一：在 Activity 中先开启一个异步线程，然后广播接收到消息后，
     *                  将数据发送给这个异步线程，然后在异步线程的 handleMessage()处理耗时操作,
     *                  这样，即使 onReceiver 执行完毕，数据却交给了异步线程操作，这个线程的生命周期同于 Activity
     */
    private void sendMsgByDefineHandlerThread(String data) {

        Message message = new Message();
        message.what = 0x11;
        message.obj = data;

        // 这里是在主线程发送的消息，所以 handler 发送消息回到 main中的 MessageQueue 中
        handlerThread.getHandler().sendMessage(message);
    }

    /**
     * 策略二：使用 IntentService处理
     * @param data
     */
    private void sendMsgByIntentService(Context context, String data) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction("com.xss.startIntentService");
        intent.putExtra("data", data);
        context.startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_test);

        initView();

        // 方式一：先启动异步消息处理线程，这一步同 IntentService 很像 createDefineHandlerThread();
        tv_msg.setText("--onCreate: begin to start myHandlerThread !--\n");
        handlerThread = new MyHandlerThread("HandlerThread");
        mainHandler = new MainHandler();
        handlerThread.setMainHandler(mainHandler);
        handlerThread.start();

        // 1. 定义广播
        tv_msg.append("--onCreate: create and register broadcastReceiver !--\n");
        downloadBroadcastReceiver = new DownloadBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("");
        intentFilter.addAction(ACTION_SEND_MSG_BY_MYHANDLERTHREAD);
        intentFilter.addAction(ACTION_SEND_MSG_BY_MYINTENTSERVICE);
        intentFilter.addAction(ACTION_FROM_INTENTSERVICE);
        registerReceiver(downloadBroadcastReceiver, intentFilter);
    }

    private void initView() {
        tv_msg = (TextView) findViewById(R.id.tv_msg);

        tv_msg_handler_thread = (TextView) findViewById(R.id.tv_msg_handler_thread);
        btn_send_my_handler_thread = (Button) findViewById(R.id.btn_send_my_handler_thread);
        tv_msg_intent_service = (TextView) findViewById(R.id.tv_msg_intent_service);
        btn_send_intent_service = (Button) findViewById(R.id.btn_send_intent_service);

        btn_send_my_handler_thread.setOnClickListener(this);
        btn_send_intent_service.setOnClickListener(this);

        tv_msg_intent_service.setOnClickListener(this);
    }

    private void sendBroadcast(String action) {
        // 2. 发送广播
        Intent intent = new Intent(action);
        intent.putExtra("data", "hello, This is a msg from broadcast to send to you !");
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_my_handler_thread:
                tv_msg_handler_thread.append("--onClick: begin to send broadcast !--\n");
                sendBroadcast(ACTION_SEND_MSG_BY_MYHANDLERTHREAD);
                break;

            case R.id.btn_send_intent_service:
                tv_msg_intent_service.append("--onClick: begin to send broadcast !--\n");
                sendBroadcast(ACTION_SEND_MSG_BY_MYINTENTSERVICE);
                break;

            case R.id.tv_msg_intent_service:
                Intent intent = new Intent(this, MyService.class);
                startService(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadBroadcastReceiver);
//        stopService(new Intent());
    }
}
