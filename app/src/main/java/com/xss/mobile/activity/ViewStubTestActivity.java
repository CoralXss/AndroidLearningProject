package com.xss.mobile.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xss.mobile.R;

/**
 * Created by xss on 2016/11/15.
 * desc:
 * 1、ViewStub默认是不显示的，也不占内存，加载时会被其他的布局代替才占用内存并显示，
 *      加载后如果只隐藏用ViewStub.setVisibility(View.GONE)就可以正常隐藏，但是不能释放内存。
 *
 *  2、ViewStub只能在Activity中加载一次，加载后ViewStub就被其中的layout布局替换了，
 *      不能重复加载否则会报错：ViewStub must have a non-null ViewGroup viewParent
 *  3、每次要判断ViewStub.getParent()是否为空，不为null时加载，为null不加载。
 *
 */
public class ViewStubTestActivity extends Activity {
    private ViewStub stub_import;
    private Button btn_click;

    private TextView tv_view_stub_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stub_test);

        initView();
    }

    private void showPhoneSystem() {
        tv_view_stub_content.setText("手机型号： " + Build.MODEL + ", " +
            "系统版本： " + Build.VERSION.RELEASE);
    }

    private void initView() {
        btn_click = (Button) findViewById(R.id.btn_click);
        stub_import = (ViewStub) findViewById(R.id.stub_import);

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stub_import.setVisibility(View.VISIBLE);

                // 不为null，说明是第一次加载，第二次加载就为null了
//                if (null != stub_import.getParent()) {
                    stub_import.inflate();

                    tv_view_stub_content = (TextView) findViewById(R.id.tv_view_stub_content);
                    showPhoneSystem();
//                }
            }
        });
    }
}
