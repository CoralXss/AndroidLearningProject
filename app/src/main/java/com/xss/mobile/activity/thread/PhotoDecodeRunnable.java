package com.xss.mobile.activity.thread;

import android.os.Process;

/**
 * Created by xss on 2016/11/15.
 */
public class PhotoDecodeRunnable implements Runnable {
    @Override
    public void run() {

        // 把当前的线程变成后台执行的线程
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

    }
}
