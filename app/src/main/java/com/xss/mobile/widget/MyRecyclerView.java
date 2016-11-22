package com.xss.mobile.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xss on 2016/11/2.
 */
public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 让外部ScrollView不拦截内部子类的 竖直滑动事件，外部的还是交给ScrollView，内部的交给ListView
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 按下和移动时设置父控件不拦截事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 点击事件结束后，取消不拦截
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(e);
    }
}
