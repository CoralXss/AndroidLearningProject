package com.xss.mobile.activity.countdown;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xss.mobile.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCountDownActivity extends AppCompatActivity implements View.OnClickListener {
//    private Button btn_by_handler_post_delay;
//    private Button btn_by_handler_thread;
//    private Button btn_by_timer_task;
//    private Button btn_by_count_down_timer;
    private TextView tv_time;

    private Handler mHandler;
    private int TIME = 5;

    private static final int MSG_WHAT_THREAD_SLEEP = 0x11;

    private Handler mThreadHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (TIME == -1) {
                tv_time.setText("倒计时结束");
            } else {
                tv_time.setText("倒计时开始：" + TIME);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_count_down);

        mHandler = new Handler();

        tv_time = (TextView) findViewById(R.id.tv_time);

        findViewById(R.id.btn_by_handler_post_delay).setOnClickListener(this);
        findViewById(R.id.btn_by_handler_thread).setOnClickListener(this);
        findViewById(R.id.btn_by_timer_task).setOnClickListener(this);
        findViewById(R.id.btn_by_count_down_timer).setOnClickListener(this);
    }

    /**
     * 方式一：最简单的方式，通过不断 Handler.postDelayed()进行
     */
    private void byHandlerPostDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TIME == -1) {
                    tv_time.setText("倒计时结束");
                } else {
                    tv_time.setText("倒计时开始：" + TIME);
                    TIME--;
                    mHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    /**
     * 方式二：子线程执行每休眠一秒，发送一个 Message 给 Handler
     */
    private void byHandlerThreadSleep() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message msg = Message.obtain();
                        msg.what = TIME;
                        mThreadHandler.sendMessage(msg);

                        // 延迟需要放在此处，才能看到初始效果
                        Thread.sleep(1000);

                        TIME--;
                        if (TIME == -1) {
                            break;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 方式三：通过 Timer + TimerTask 方式实现倒计时
     */
    private void byTimerTask() {
        final Timer timer = new Timer();

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TIME == -1) {
                            tv_time.setText("倒计时结束");
                            timer.cancel();
                        } else {
                            tv_time.setText("倒计时开始：" + TIME);
                            TIME--;
                        }
                    }
                });
            }
        };

        timer.schedule(task, 1000, 1000);
    }

    /**
     * 方式四：通过 CountDownTimer 方式实现倒计时
     */
    private void byCountDownTimer() {
        // 内部实现 Handler
        MyCountDownTimer countDownTimer = new MyCountDownTimer(TIME * 1000, 1000);
        countDownTimer.start();
    }

    private class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.                        最大时间点
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.  间隔时间，每隔多久计时一次
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_time.setText("倒计时开始：" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            tv_time.setText("倒计时结束");
            this.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_by_handler_post_delay:
                byHandlerPostDelay();
                break;
            case R.id.btn_by_handler_thread:
                byHandlerThreadSleep();
                break;
            case R.id.btn_by_timer_task:
                byTimerTask();
                break;
            case R.id.btn_by_count_down_timer:
                byCountDownTimer();
                break;
        }
    }
}
