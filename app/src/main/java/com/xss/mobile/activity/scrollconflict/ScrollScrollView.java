package com.xss.mobile.activity.scrollconflict;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by xss on 2017/6/27.
 * This is for scrollview nests in editText .
 */

public class ScrollScrollView extends ScrollView {
    public ScrollScrollView(Context context) {
        super(context);
    }

    public ScrollScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        // down 事件不能被父控件拦截
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            return false;
//        }
        return super.onInterceptTouchEvent(ev);
    }
}
