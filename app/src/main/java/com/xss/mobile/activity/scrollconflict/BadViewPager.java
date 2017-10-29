package com.xss.mobile.activity.scrollconflict;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by xss on 2017/6/28.
 */

public class BadViewPager extends ViewPager {
    private int mTouchSlop;

    public BadViewPager(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public BadViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private float mLastX, mLastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;

        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                // 调用 ViewPager 的 onInterceptTouchEvent 方法初始化 mActivePointerId
                // 缺少这一句初始化 mActivePointerId，拦截了也无法消耗，执行横向滑动
                super.onInterceptTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:

                float dx = Math.abs(x - mLastX);
                float dy = Math.abs(y - mLastY);

                Log.e("intercept", dx + ", " + dy + ", " + mTouchSlop);

                if (dx > mTouchSlop && Float.compare(dx * 0.5f, dy) > 0) {
                    intercepted = true;
                } else {
                    intercepted = false;

                }

                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }

        mLastX = x;
        mLastY = y;

        return intercepted;
    }
}
