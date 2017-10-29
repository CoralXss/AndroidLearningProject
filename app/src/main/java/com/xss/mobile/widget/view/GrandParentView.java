package com.xss.mobile.widget.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xss on 2017/3/31.
 */

public class GrandParentView extends ViewGroup {

    private String TAG = GrandParentView.class.getSimpleName();

    public GrandParentView(Context context) {
        super(context);
    }

    public GrandParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GrandParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GrandParentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 宽度 = 水平方向所有 childView 宽度之和， 高度 = 每行中 最高的 childView 之和
        int maxWidth = 0, maxHeight = 0;
        int leftWidth = 0, leftHeight = 0;

        // 为子View的大小 和 父亲specSize 的 最小值
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            // 设置为 GONE，不占空间，就当不存在
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            // 测量子View
            child.measure(widthMeasureSpec, heightMeasureSpec);

            //
            maxWidth += child.getMeasuredWidth();

            maxHeight += child.getMeasuredHeight();
        }

        LayoutParams lp = getLayoutParams();

        setMeasuredDimension(lp.width, lp.height);

    }

    private int measureWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        LayoutParams lp = getLayoutParams();

        int resultWidth = 0;

        if (specMode == MeasureSpec.EXACTLY) {
            if (lp.width == LayoutParams.MATCH_PARENT) {  // 为父View大小
                resultWidth = specSize;

            } else if (lp.width == LayoutParams.WRAP_CONTENT) {
                // 为子View的大小 和 父亲specSize 的 最小值
                for (int i = 0; i < getChildCount(); i++) {

                }
            } else {
                resultWidth = lp.width;
            }

        } else if (specMode == MeasureSpec.AT_MOST) {
            resultWidth = specSize < lp.width ? specSize : lp.width;
        } else {
            resultWidth = specSize;
        }

        return resultWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        LayoutParams lp = getLayoutParams();

        int resultHeight = 0;

        if (specMode == MeasureSpec.EXACTLY) {
            if (lp.height == LayoutParams.MATCH_PARENT) {  // 为父View大小
                resultHeight = specSize;

            } else if (lp.height == LayoutParams.WRAP_CONTENT) {
                // 为子View的大小 和 父亲specSize 的 最小值
                for (int i = 0; i < getChildCount(); i++) {

                }
            } else {
                resultHeight = lp.height;
            }

        } else if (specMode == MeasureSpec.AT_MOST) {
            resultHeight = specSize < lp.height ? specSize : lp.height;
        } else {
            resultHeight = specSize;
        }

        return resultHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int padding = 20;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null) {
                int childHeight = child.getMeasuredHeight();
                int childWidth = child.getMeasuredWidth();
                child.layout(l, t, l + childWidth, t + childHeight);

                b += padding;
            }
        }
    }

    /**
     * ViewGroup的 dispatchTouchEvent 分发事件逻辑：
     * 1. 先调用自己的 onInterceptTouchEvent()判断是否拦截事件；
     * 2. 如果不拦截，找到所有的 childView，并分发给child，有需要的 child View 就会消费这次事件；
     * 3. 如果 child View 不消耗事件，就交给自己处理，把ViewGroup当做一个普通的 View，调用 super.dispatchTouchEvent() 【注意，这里的 super代表的是 View，因为 ViewGroup是View的子类】;
     * 4. 查看 View.dispatchTouchEvent() ，简单的逻辑就是：
     *    a. 如果ViewGroup自己设置 OnTouchListener 并且设置为 enable，就返回 true，表示自己要消耗此事件；
     *    b. 如果上述条件不成立，就会调用自己的 onTouchEvent() 方法查看是否返回 true，为true表示要消耗；
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 是否拦截
        boolean intercepted = onInterceptTouchEvent(ev);

        View targetView = null;
        boolean handled = false;

        // 如果不拦截遍历所有的 child，判断是否有分发
        if (!intercepted) {

            // 没有子 View的情况，调用 View.dispatchTouchEvent()方法，这里的super不是父 View
            View child = getChildAt(0);
            if (child == null) {
                handled = super.dispatchTouchEvent(ev);
            } else {
                // 如果有子 View，再调用 child的分发方法
                handled = child.dispatchTouchEvent(ev);
            }

            if (handled) {
                targetView = child;
            }
        }

        // 如果没有 子View消费事件，那么该ViewGroup就当做普通的 View来处理
        if (targetView == null) {
            handled = super.dispatchTouchEvent(ev);
        }

        return handled;
//        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
