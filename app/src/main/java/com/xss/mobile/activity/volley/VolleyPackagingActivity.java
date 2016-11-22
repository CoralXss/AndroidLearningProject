package com.xss.mobile.activity.volley;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xss.mobile.R;
import com.xss.mobile.network.volley.NetApiManager;
import com.xss.mobile.network.volley.RequestListener;

/**
 * Created by xss on 2016/11/18.
 */
public class VolleyPackagingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_test);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetApiManager.getInstance(VolleyPackagingActivity.this).testPost("xss", new RequestListener() {
                    @Override
                    public void onPreExecute() {
                        Toast.makeText(VolleyPackagingActivity.this, "正在请求数据...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String data) {
                        Toast.makeText(VolleyPackagingActivity.this, "请求数据成功！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String code, String msg) {
//                        Toast.makeText(VolleyPackagingActivity.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
        });
    }


}
