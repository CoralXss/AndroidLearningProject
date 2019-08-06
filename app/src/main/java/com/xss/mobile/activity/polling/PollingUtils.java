package com.xss.mobile.activity.polling;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xss on 2017/10/30.
 */

public class PollingUtils {

    private int pollingInterval = 20 * 1000; // 20s
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static volatile PollingUtils sInstance;

    private PollingUtils() {

    }

    public static PollingUtils getInstance() {
        if (sInstance == null) {
            synchronized (PollingUtils.class) {
                if (sInstance == null) {
                    sInstance = new PollingUtils();
                }
            }
        }
        return sInstance;
    }

    public void startPolling() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.print("Hello world !");
            }
        }, 0, pollingInterval, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        PollingUtils.getInstance().startPolling();
    }

}
