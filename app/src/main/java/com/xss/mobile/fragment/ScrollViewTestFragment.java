package com.xss.mobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xss.mobile.R;
import com.xss.mobile.widget.view.NoScrollListVIew;

import java.util.List;

/**
 * Created by xss on 2017/2/20.
 */

public class ScrollViewTestFragment extends Fragment {
    private View view;
    private NoScrollListVIew lv_list;

    public NoScrollListVIew getListView() {
        return lv_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_scroll_view_test, container, false);

        initView();

        initData();

        return view;
    }

    private void initView() {
        lv_list = (NoScrollListVIew) view.findViewById(R.id.lv_list);

//        lv_list.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                lv_list.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        // 解决 EditText在ScrollView中滑动冲突
        lv_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // true 不拦截
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    // false- disallowIntercept 允许拦截
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }

                return false;
            }
        });
    }

    private void initData() {
        String[] datas = new String[50];
        for (int i = 0; i < datas.length; i++) {
            datas[i] = "items position = " + i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, datas);
        lv_list.setAdapter(adapter);
    }
}
