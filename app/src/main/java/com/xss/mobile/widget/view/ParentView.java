package com.xss.mobile.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xss on 2017/3/31.
 *
 * 自定义 ViewGroup 中如何存放的是自定义的 View，就需要测量 自定义View的宽和高
 */

public class ParentView extends ViewGroup {

    private String TAG = ParentView.class.getSimpleName();

    public ParentView(Context context) {
        super(context);
    }

    public ParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        setMeasuredDimension(maxWidth, maxHeight);

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

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            child.layout(l + padding, t, l + childWidth, t + childHeight);

            l += childWidth;
            t += childHeight;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
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
