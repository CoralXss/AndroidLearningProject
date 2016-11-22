package com.xss.mobile.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xss.mobile.R;

public class LooperTestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_test);

        findViewById(R.id.btn_start_thread).setOnClickListener(this);
        findViewById(R.id.btn_send_message).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_thread:
                new LooperThread().start();
                break;
            case R.id.btn_send_message:
                if (null != handler) {
                    Message msg = new Message();
                    msg.obj = "Hello，World";
                    msg.what = 0x11;
                    handler.sendMessage(msg);
                }
                break;
        }
    }

    private Handler handler;
    // RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
    class LooperThread extends Thread {

        @Override
        public void run() {
            Looper.prepare();

            // 在构造handler之前，必须执行Looper.prepare()，并且只能执行一次
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x11) {
                        Toast.makeText(LooperTestActivity.this, "" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            Looper.loop();
        }
    }
}
