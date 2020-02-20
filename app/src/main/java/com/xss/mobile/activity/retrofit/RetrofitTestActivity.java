package com.xss.mobile.activity.retrofit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xss.mobile.R;
import com.xss.mobile.entity.MovieEntity;
import com.xss.mobile.network.okhttp.ApiService;
import com.xss.mobile.network.retrofit.RetrofitManager;

import java.io.File;

//import butterknife.BindView;
//import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitTestActivity extends AppCompatActivity {
//    @BindView(R.id.click_me_BN)
    protected Button click_me_BN;
//    @BindView(R.id.result_TV)
    protected TextView result_TV;

//    @BindView(R.id.rxjava_BN)
    protected Button rxjava_BN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_test);
//        ButterKnife.bind(this);

        click_me_BN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovie();
            }
        });

        rxjava_BN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                subscriber();

                getMovie2();
            }
        });
    }

    /**
     *
     */
    private void subscriber() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e("RxJava", "Observable: begin sending...");
                subscriber.onNext("Hi, I'm Lily");        // 发送数据
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e("RxJava", "Observer: onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("RxJava", "Observer: onError");
            }

            @Override
            public void onNext(String s) {
                Log.e("RxJava", "Observer: onReceived " + s); // 接收数据
            }
        };

        // 关联发送者和接受者
        Subscription subscription = observable.subscribe(observer);

        // 取消订阅，之后就不会收到 Observable 发送的消息
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }


        /**
         * 点击按钮，结果：
         *  RxJava: Observable: begin sending...
         *  RxJava: Observer: onReceived Hi, I'm Lily
         *
         *  异步请求：一个发送源，一个接收源
         */
    }

    /**
     * 只用 Retrofit 的情况:
     * 1. 原生态，没有封装的 Retrofit 写网络请求的代码。
     * 2. 未添加：1）请求加载框；2）统一处理返回数据格式；3）错误返回统一处理
     *
     * 简单来说分四步：
     */
    private void getMovie() {
        String baseUrl = "https://api.douban.com/v2/movie/";

        // 1. 创建 Retrofit , 封装 请求 url 数据转换格式
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 2. 创建 ApiService 实例 - 动态代理
        ApiService apiService = retrofit.create(ApiService.class);

        // 3. 获取 请求接口返回
        Call<MovieEntity> call = apiService.getTopMovie(0, 10);
        // 4. 开始异步请求
        call.enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                result_TV.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable t) {
                result_TV.setText(t.getMessage());
            }
        });
    }

    /**
     * Retrofit 支持 RxJava：
     * 1. 只需添加依赖：'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'
     *
     */
    private void getMovie2() {
        String baseUrl = "https://api.douban.com/v2/movie/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getTopMovie2(0, 10)
                .subscribeOn(Schedulers.io())                // 指定 subscribe() 发生在 IO线程 子线程中发送事件（多次调用 subscribeOn 仅第一次有效）
                .observeOn(AndroidSchedulers.mainThread())   // 指定 Subscriber 发生在主线程 主线程中接收事件（每调用一次observeOn,就切换一次下游接收事件线程）
                .subscribe(new Subscriber<MovieEntity>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RetrofitTestActivity.this, "Get Top 10 Movie Completed !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        result_TV.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieEntity movieEntity) {
                        result_TV.setText(movieEntity.toString());
                    }
                });
    }

    /**
     * 稍作封装的请求
     */
    private void getMovie3() {

        Subscriber<MovieEntity> subscriber = new Subscriber<MovieEntity>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RetrofitTestActivity.this, "Get Top 10 Movie Completed !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                result_TV.setText(e.getMessage());
            }

            @Override
            public void onNext(MovieEntity movieEntity) {
                result_TV.setText(movieEntity.toString());
            }
        };

        RetrofitManager.getInstance().getTopMovie(subscriber, 10, 20);
    }

    /**
     * 使用 Retrofit 上传文件
     */
    private void uploadFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);

        String desc = "Hello, this is description speaking";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), desc);


        String baseUrl = "https://api.douban.com/v2/movie/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.uploadFile(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
