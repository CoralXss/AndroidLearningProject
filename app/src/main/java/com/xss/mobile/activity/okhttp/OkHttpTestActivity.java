package com.xss.mobile.activity.okhttp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.xss.mobile.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xss on 2016/11/18.
 */
public class OkHttpTestActivity extends Activity {
    private static final String TAG = OkHttpTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_test);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGet();
            }
        });

    }

    private void testGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1、获取封装好的OkHttp客户端-OkHttpClient
                OkHttpClient client = new OkHttpClient();
                try {
                    // 2、http请求体-Request
                    Request request = new Request.Builder().url("http://www.baidu.com").build();
                    // 没有添加 Callback，同步请求，在主线程中做耗时操作：NetworkOnMainThreadException
                    // 3、http响应体-Response
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        Log.d(TAG, content);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private void testPost() {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, "json");
        Request request = new Request.Builder().url("url").post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();

            String content = response.body().string();
            Log.d(TAG, content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
