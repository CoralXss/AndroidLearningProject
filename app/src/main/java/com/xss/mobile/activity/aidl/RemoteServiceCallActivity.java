package com.xss.mobile.activity.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.xss.mobile.IMyAidlInterface;
import com.xss.mobile.R;
import com.xss.mobile.service.RemoteService;

/**
 * Created by xss on 2016/12/21.
 * desc: 客户端：调用服务端代码
 */
public class RemoteServiceCallActivity extends Activity {
    private static final String TAG = RemoteServiceCallActivity.class.getSimpleName();

    // 由AIDL文件生成的 Java类
    private IMyAidlInterface myAidlInterface;
    // 标志当前与服务端链接状况。true为已连接
    private boolean mBound = false;

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            mBound = true;

            if (myAidlInterface != null) {
                try {
                    int result = myAidlInterface.getData();
                    Log.e(TAG, "get data from service by aidl : " + result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViewById(R.id.btn_to_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBound) {  // 没有连接就打开远程service
                    Intent intent = new Intent("com.xss.mobile.IMyAidlInterface");
                    intent.setPackage("com.xss.mobile");
                    bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
                }
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConn);
            mBound = false;
        }
    }
}
