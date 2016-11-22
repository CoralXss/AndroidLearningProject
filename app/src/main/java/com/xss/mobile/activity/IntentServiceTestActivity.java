package com.xss.mobile.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xss.mobile.R;
import com.xss.mobile.service.RSSPullIntentService;

/**
 * Created by xss on 2016/11/14.
 */
public class IntentServiceTestActivity extends Activity {
    private static final String TAG = IntentServiceTestActivity.class.getSimpleName();

    private ResponseReceiver broadcastReceiver;

    public static void toHere(Context context) {
        Intent intent = new Intent(context, IntentServiceTestActivity.class);
        context.startActivity(intent);
    }

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getDataString();
            Toast.makeText(context, TextUtils.isEmpty(data) ? "empty" : data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service_test);

        IntentFilter filter = new IntentFilter("com.xss.mobile.IntentServiceBroadcastReceiver");
        broadcastReceiver = new ResponseReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntentServiceTestActivity.this, RSSPullIntentService.class);
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);  //
        super.onDestroy();
    }
}
