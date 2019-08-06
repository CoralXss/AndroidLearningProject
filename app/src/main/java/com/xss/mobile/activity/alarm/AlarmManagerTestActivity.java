package com.xss.mobile.activity.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xss.mobile.R;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmManagerTestActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final long INTERVAL = 300l; // 设置30s
    private final Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager_test);

        // 定时设置300s后打印一次就不打印（设置在某个时间点打印，若需要长连接，需要自定义轮询效果）
        initAlarmManager();

        findViewById(R.id.btn_start_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast("开始计时");
//                TimerTask task = new TimerTask() {
//                    @Override
//                    public void run() {
//                        sendBroadcast(new Intent("com.xss.mobiles.ACTION_CHECK_POLLING"));
//                    }
//                };
//                timer.schedule(task, 0, 2000);

                cancelAlarm();
            }
        });
    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
        showToast("取消闹钟");
    }

    @Override
    protected void onDestroy() {
        // 这里 timer若不取消则会一直发广播
        timer.cancel();
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 三种Alarm类型：
     *  ELAPSED_REALTIME：使用相对时间，SystemClock.elapsedRealtime()获取从开机到现在时间的毫秒数（包括手机睡眠时间），设备休眠时不会唤醒设备
     *  ELAPSED_REALTIME_WAKEUP：同ELAPSED_REALTIME，只是设备休眠时会唤醒设备
     *  RTC：使用绝对时间，System.currentTimeMillis()获取，设备休眠时不会唤醒设备
     *  RTC_WAKEUP：同RET，只是设备休眠时会唤醒设备
     *
     * 一次性闹钟：set()方法
     * 重复性闹钟：setRepeating()
     *
     * AlarmManager 适合Android中的定时任务，因为具有唤醒CPU功能，
     *     可以保证每次执行任务时CPU能正常工作，或者说CPU处于休眠时被注册的Alarm会被保留。
     */
    private void initAlarmManager() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

//        if (Build.VERSION.SDK_INT >= 23) {
              // 性能影响，后台不耗电
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime() + INTERVAL,
//                    pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime() + INTERVAL,
//                    pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime() + INTERVAL,
//                    pendingIntent);
//        }

        // 这里在开启重复定时器后，若在页面销毁时没有取消会一直重复执行；进程在后台也会执行；杀死进程，不会执行
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                INTERVAL,
                pendingIntent);

    }
}
