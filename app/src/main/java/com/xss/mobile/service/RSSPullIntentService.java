package com.xss.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by xss on 2016/11/14.
 * desc: 执行简单后台任务操作的理想选择
 * 一、IntentService的局限性
 *  1、不可以直接同UI交互。为了把执行结果在UI上显示，需要把结果返回给Activity；
 *  2、工作任务队列是顺序执行的。等待前一个任务执行完毕才开始执行下一个任务；
 *  3、正在执行的任务无法打断。
 */
public class RSSPullIntentService extends IntentService {

    public RSSPullIntentService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String data = intent.getDataString();

        // 回传结果给activity
        Intent localIntent = new Intent("com.xss.mobile.IntentServiceBroadcastReceiver");
        localIntent.putExtra("status", "is OK");
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
