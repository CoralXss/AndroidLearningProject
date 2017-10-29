package com.xss.mobile.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.xss.mobile.R;
import com.xss.mobile.fragment.ScrollViewTestFragment;
import com.xss.mobile.widget.view.NoScrollListVIew;

public class ScrollViewTestActivity extends FragmentActivity {
    private ScrollView scroll_view_test;

    private NoScrollListVIew lv_list;

    ScrollViewTestFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view_test);

        initView();
    }

    private void initView() {
        scroll_view_test = (ScrollView) findViewById(R.id.scroll_view_test);
        lv_list = (NoScrollListVIew) findViewById(R.id.lv_list);
        scroll_view_test.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mFragment = (ScrollViewTestFragment) getSupportFragmentManager().findFragmentByTag("message");

//        // 解决 EditText在ScrollView中滑动冲突
//        mFragment.getListView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                // true 不拦截
//                scroll_view_test.requestDisallowInterceptTouchEvent(false);
//                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
//                    // false- disallowIntercept 允许拦截
//                    scroll_view_test.requestDisallowInterceptTouchEvent(true);
//                }
//
//                return false;
//            }
//        });
    }
}
