package com.xss.mobile.activity.scrollconflict;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xss.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动冲突情景
 * 1. ScrollView 中嵌套 EditText，当文本长度超过 EditText 高度时，滑动焦点被 外部 ScrollView 获取；
 * 2.
 */
public class ScrollConflictActivity extends AppCompatActivity {

    private EditText edt_content;

    private ScrollEditText sl_edt_content;

    private LinearLayout ll_content;

    private ListView lv_list, lv_list_1;

    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict);

        sl_edt_content = (ScrollEditText) findViewById(R.id.sl_edt_content);
        sl_edt_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        initData();
//        initView();

    }

    private void initView() {
//        edt_content = (EditText) findViewById(R.id.edt_content);
//        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        lv_list = (ListView) findViewById(R.id.lv_list);
//        lv_list_1 = (ListView) findViewById(R.id.lv_list_1);

        initContent();

//        initEvent();

    }

    private void initContent() {
        DataListAdapter adapter = new DataListAdapter(this, list);
        lv_list.setAdapter(adapter);

        lv_list_1.setAdapter(adapter);

//        ll_content.removeAllViews();
//        for (String s: list) {
//            View view = LayoutInflater.from(this).inflate(R.layout.item_view_label, null);
//            TextView tv = (TextView) view;
//            tv.setText(s);
//
//            ll_content.addView(tv);
//        }
    }


//    private  View childView;
//    boolean handled;
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (!onInterceptTouchEvent(event)) {
//            // 下发给所有子View，看是否有分发
//            if (childView != null) {
//                // 通过子View分发方法判断是否消耗了事件（递归，可能为ViewGroup或者View）
//                handled = childView.dispatchTouchEvent(event);
//            } else {
//                // 没有子View，把自己当做普通View
//                handled = super.dispatchTouchEvent(event);
//            }
//        }
//        return handled;
//    }


    private void initEvent() {
        edt_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 不许父view 拦截
                requestParentDisallowInterceptTouchEvent(true);

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    requestParentDisallowInterceptTouchEvent(false);
                }

                return false;
            }
        });
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = edt_content.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }


    // 向listView 中加载100条数据，造成卡顿时如何优化
    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("Will it be 卡顿 ?");
        }
    }
}
