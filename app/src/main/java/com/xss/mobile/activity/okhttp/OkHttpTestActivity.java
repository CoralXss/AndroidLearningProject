package com.xss.mobile.activity.okhttp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xss.mobile.R;
import com.xss.mobile.network.okhttp.ApiService;
import com.xss.mobile.network.okhttp.OkHttpManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xss on 2016/11/18.
 */
public class OkHttpTestActivity extends Activity {
    private static final String TAG = OkHttpTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_test);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGet();
            }
        });

        findViewById(R.id.btn_click_retrofit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofit();
            }
        });

    }

    private void testRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<String> result = apiService.login("s1001");

        result.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void testGet() {
        Log.d(TAG, "click");

        OkHttpManager.getInstance().testGet();
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
