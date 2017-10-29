package com.xss.mobile.activity.scrollconflict;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.EditText;

/**
 * Created by xss on 2017/6/27.
 * 内部拦截法：父 View 不拦截任何事件，都分发给子View 处理，子View 根据需要决定是自己消费还是父View 消费
 */

public class ScrollEditText extends EditText {

    public ScrollEditText(Context context) {
        super(context);
    }

    public ScrollEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int mLastX, mLastY;
    private int mTouchSlop;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
////                requestParentDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
//                if (Math.abs(y - mLastY) < mTouchSlop) {
////                    requestParentDisallowInterceptTouchEvent(false);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//            default:
//                break;
//        }
//        mLastX = x;
//        mLastY = y;

        return super.dispatchTouchEvent(event);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercepted) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercepted);
        }
    }
}
