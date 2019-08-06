package com.xss.mobile.activity.okhttp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xss.mobile.R;
import com.xss.mobile.network.okhttp.ApiService;
import com.xss.mobile.network.okhttp.OkHttpManager;
import com.xss.mobile.network.retrofit.RetrofitManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
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
//                testRetrofit();
                testAuth();
            }
        });

    }

    private void testAuth() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);

                        if (response.code() == 302) {
                            Log.e("test", response.headers().toString());

                            String location = response.header("Location");

                            Headers headers = response.headers();
                            if (headers != null && headers.size() != 0) {
                                AuthRequest.getInstance().setLocation(headers.get("Location"));
                            }

                            // 重新构造 request(去掉重定向地址不去重定向 )
                            response.header("Location", null);
                        }

                        return response;
                    }
                })
//                .followSslRedirects(false)
//                .followRedirects(false)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.12.75.142:8099")  // https://open-api-shop.alpha.elenet.me
//                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        AuthRequest request = new AuthRequest();
        Call<ResponseBody> result = apiService.auth();

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG, AuthRequest.getInstance().getLocation());
                Response r = response.raw();

//                Log.e(TAG, "success: " + r.toString()
//                        + (r.headers().toString() == null ? "null" : r.headers().toString()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "faild");
            }
        });

//        result.enqueue(new Callback<Response>() {
//
//            @Override
//            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                Log.e(TAG, "success: " + response.toString());
//                if (response.headers() != null) {
//                    Log.e(TAG, "headers: " + response.headers().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response> call, Throwable t) {
//                Log.e(TAG, "fail: ");
//            }
//        });
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
