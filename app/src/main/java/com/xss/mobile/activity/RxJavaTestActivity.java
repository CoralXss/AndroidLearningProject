package com.xss.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.widget.MyLinearLayout;

/**
 * Created by xss on 2016/10/14.
 */
public class RxJavaTestActivity extends Activity {
    private String TAG = "RxJavaTestActivity";

    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        Log.e(TAG, "onCreate");

//        tv_content = (TextView) findViewById(R.id.tv_content);
//
//        findViewById(R.id.btn_parser).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}
