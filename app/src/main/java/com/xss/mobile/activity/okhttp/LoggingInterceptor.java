package com.xss.mobile.activity.okhttp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xss on 2017/6/8.
 */

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

//        long t1 = System.nanoTime();
//        loggerError(String.format("Sending request %s on %s%n%s",
//                request.url(), chain.connection(), request.headers()));
//
        Response response = chain.proceed(request);

        if (response.code() == 302) {
            Log.e("test", response.headers().toString());
        }

//        long t2 = System.nanoTime();
//        loggerError(String.format("Received response %s in %.1fms%n%s",
//                response.request().url(),
//                (t2 - t1) / 1e6d,
//                response.headers()));



        return response;
    }

    private void loggerError(String msg) {
        Log.e("LoggingInterceptor", msg);
    }
}
