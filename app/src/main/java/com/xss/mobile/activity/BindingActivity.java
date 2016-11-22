package com.xss.mobile.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.xss.mobile.R;
import com.xss.mobile.service.LocalBindService;
import com.xss.mobile.service.LocalStartService;

/**
 * Created by xss on 2016/10/11.
 */
public class BindingActivity extends Activity {
    private LocalBindService localService;
    boolean isBound = false;

    private String TAG = "BindingActivity";

    /** 定义服务绑定时的回调方法，用于传给bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(TAG, "onServiceConnected");
            // 已经绑定到LocalService，对Ibinder强转得到LocalService
            LocalBindService.LocalBinder binder = (LocalBindService.LocalBinder) iBinder;
            localService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG, "onServiceDisconnected");
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        Log.e(TAG, "onCreate");

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = localService.getRandomNumber();
                Log.e("BindingActivity", "number = " + num);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");

        // 绑定到Service
//        Intent intent = new Intent(this, LocalBindService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        Intent intent0 = new Intent(this, LocalStartService.class);
        startService(intent0);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");

        // 与服务解除绑定
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
