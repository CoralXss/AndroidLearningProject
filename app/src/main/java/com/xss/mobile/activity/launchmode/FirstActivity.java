package com.xss.mobile.activity.launchmode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xss.mobile.R;

public class FirstActivity extends AppCompatActivity {
    String TAG = FirstActivity.class.getSimpleName();

    private RatingBar ratingBar, ratingBar2, ratingBar3;

    String oMainMsg = "";

    TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        oMainMsg = getIntent().getStringExtra("MainMsg");

        Log.e(TAG, "onCreate: " + oMainMsg);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.append(", ");
        tv_name.append(oMainMsg);

        init();

        if (savedInstanceState != null) {
            Log.e(TAG, "onCreate saved datas");
        }

        findViewById(R.id.btn_to_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                Log.e(TAG, "1-1 hash = " + intent.hashCode());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void init() {
        /**
         * RatingBar 用法：
         * 1. 只能使用 wrap_content，设置星星总数才有效;
         * 2. 自定义背景图片：1）自定义 layer-list; 2）自定义 style；
         *                  3）这里设置 minHeight 和 maxheight 一定要和图片高度一致，不然图片显示会被拉伸
         */
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // activity在栈顶时，若以singleTop启动activity，就不会执行 onCreate只执行 onNewIntent，所以在onCreate中执行的数据请求操作需要在该方法中也执行一遍；
        // 但若Activity被系统杀死，就会调用 onCreate 而不是调用 onNewIntent，一个好的方法就是在 onCreate 和 onNewIntent方法中调用同一个数据处理的方法
        Log.e(TAG, "onNewIntent " + ((null == intent) ? "null" : ("1-1 n hash = " + intent.hashCode())));

        String msg = intent.getStringExtra("msg");
        Log.e(TAG, "msg = " + (msg == null ? "null" : msg));

        tv_name.append(",  ");
        tv_name.append(msg);

        String mainMsg = intent.getStringExtra("MainMsg");
        Log.e(TAG, "MainMsg = " + (mainMsg == null ? "null" : mainMsg) + ", create = " + oMainMsg);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
