package com.xss.mobile.activity.dagger2;

import android.content.Context;
import android.widget.Toast;

import com.xss.mobile.MyApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by xss on 2017/10/11.
 */

//@Singleton
public class LoginManager {

    private static LoginManager sInstance;

    public static LoginManager getInstance() {
        return sInstance;
    }

    @Inject
    public LoginManager() {
        sInstance = this;
    }

    public void login(Context context) {
        Toast.makeText(context, "login success", Toast.LENGTH_SHORT).show();
    }
}
