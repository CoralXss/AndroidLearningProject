package com.xss.mobile.activity.scrollconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.xss.mobile.utils.ViewUtil;

/**
 * Created by xss on 2017/5/4.
 * desc: 外部方向和内部滑动方向不一致造成的滑动冲突 —— 外部拦截方式处理（父控件拦截处理）
 */

public class MyHorizontalScrollView extends ViewGroup {
    private static final String TAG = MyHorizontalScrollView.class.getSimpleName();

    private VelocityTracker velocityTracker;
    private Scroller scroller;

    // 上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    // 上次拦截滑动的坐标
    private int mLastInterceptedX = 0;
    private int mLastInterceptedY = 0;

    public MyHorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        velocityTracker = VelocityTracker.obtain();
        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = 0;
        int measuredHeight = 0;

        // 先测量所有的子 View
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        View childView = getChildAt(0);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            // 宽度 = 所有子 View 宽度之和
            measuredWidth = childCount * childView.getMeasuredWidth();
            measuredHeight = childView.getMeasuredHeight();

        } else if (heightMode == MeasureSpec.AT_MOST) {
            measuredWidth = widthSize;
            measuredHeight = childView.getMeasuredHeight();

        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = childCount * childView.getMeasuredWidth();
            measuredHeight = heightSize;

        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, l + ", " + t + ", " + r + ", " + b);

        int childCount = getChildCount();
        int childLeft = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                break;
            }
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            childView.layout(childLeft, 0, childLeft + childWidth, childHeight);

            childLeft += childWidth;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 拦截事件：当水平滑动的距离 > 垂直滑动的距离，父控件拦截，反之，交由子控件处理
     *  默认，如果直接 return super.onInterceptTouchEvent(ev); 则滑动事件全交由子控件 ListView 处理
     *
     *  onInterceptTouchEvent 和 onTouchEvent 有很复杂的联系，在此方法中可处理（拦截） down 方法：
     *  1. down 方法可以被子 View 处理，也可以被自己的 onTouchEvent() 处理；
     *      如要返回 true，则需实现 onTouchEvent() 方法；
     *      如果onTouchEvent 也返回 true，就不会在 onInterceptTouch() 方法中执行其他如 MOVE UP 等方法，任何触摸操作都只会在 onTouchEvent 中执行；
     *  2. 若方法返回 false，up 或者 up 之后的方法首先会在这里分发，然后丢给 目标 target 的 onTouchEvent() 方法；
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        Log.e(TAG, "intercepted: x = " + x + ", y = " + y
                + " / lastX = " + mLastInterceptedX + ", lastY = " + mLastInterceptedY);
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            // 按下这个动作，不能被拦截，拦截了之后的 MODE 和 UP 事件都会被拦截
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dealtX = x - mLastInterceptedX;
                int dealtY = y - mLastInterceptedY;
                if (Math.abs(dealtX) > Math.abs(dealtY)) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;

        mLastInterceptedX = x;
        mLastInterceptedY = y;

        return intercepted;    //super.onInterceptTouchEvent(ev);

    }


    /**
     * 当事件被 HorizontalScrollView 拦截才会执行 onTouchEvent 方法，代表"我要消费掉该事件"，所以在该方法中要处理水平滑动这个动作
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将当前的移动事件添加给 VelocityTracker 对象，可计算当前的速度
        velocityTracker.addMovement(event);

        int screenWidth = ViewUtil.getScreenWidth(getContext());

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                int dealtX = x - mLastX;
                if ((getScrollX() >= dealtX) && (getScrollX() - dealtX <= getMeasuredWidth() - screenWidth)) {
                    // 开始滑动
                    scrollBy(-dealtX, 0);
                }
                break;

            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                // 根据速率判断 滑动的方向
                if (Math.abs(xVelocity) > 50) {
                    if (xVelocity > 0) {
                        Log.e(TAG, "快速向右滑");
                    } else {
                        Log.e(TAG, "快速向左滑");
                    }
                }
                velocityTracker.clear();
                break;

            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

}
