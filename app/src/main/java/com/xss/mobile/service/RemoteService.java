package com.xss.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.xss.mobile.IMyAidlInterface;

/**
 * Created by xss on 2016/10/11.
 * desc: 服务端的 Service，在此实现AIDL中定义的方法接口的具体逻辑，然后在客户端（其他应用）调用这些方法，从而达到跨进程通信的目的
 */
public class RemoteService extends Service {

    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public int getData() throws RemoteException {
            return 100;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }
}
