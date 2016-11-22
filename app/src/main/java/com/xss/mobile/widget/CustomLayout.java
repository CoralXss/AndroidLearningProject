package com.xss.mobile.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xss.mobile.R;

/**
 * Created by xss on 2016/10/14.
 */
public class CustomLayout extends ViewGroup {
    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int mLeftWidth;  // 左侧被childView使用的所有空间
    private int mRightWidth; // 右侧被childView使用的所有空间

    // 通过子View的gravity计算child的frames框架
    private Rect mTempContainerRect = new Rect();
    private Rect mTempChildRect = new Rect();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        mLeftWidth = 0;
        mRightWidth = 0;

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // 测量childView
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                CustomLayoutParams lp = (CustomLayoutParams) child.getLayoutParams();
                if (lp.position == CustomLayoutParams.POSITION_LEFT) {
                    mLeftWidth += Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else if (lp.position == CustomLayoutParams.POSITION_RIGHT) {
                    mRightWidth += Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else {
                    maxWidth = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                    childState = combineMeasuredStates(childState, child.getMeasuredState());
                }
            }
        }

        maxWidth += mLeftWidth + mRightWidth;
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();
        // This is the middle region inside of the gutter.
        final int middleLeft = leftPos + mLeftWidth;
        final int middleRight = rightPos - mRightWidth;
        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final CustomLayoutParams lp = (CustomLayoutParams) child.getLayoutParams();
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                // Compute the frame in which we are placing this child.
                if (lp.position == CustomLayoutParams.POSITION_LEFT) {
                    mTempContainerRect.left = leftPos + lp.leftMargin;
                    mTempContainerRect.right = leftPos + width + lp.rightMargin;
                    leftPos = mTempContainerRect.right;
                } else if (lp.position == CustomLayoutParams.POSITION_RIGHT) {
                    mTempContainerRect.right = rightPos - lp.rightMargin;
                    mTempContainerRect.left = rightPos - width - lp.leftMargin;
                    rightPos = mTempContainerRect.left;
                } else {
                    mTempContainerRect.left = middleLeft + lp.leftMargin;
                    mTempContainerRect.right = middleRight - lp.rightMargin;
                }
                mTempContainerRect.top = parentTop + lp.topMargin;
                mTempContainerRect.bottom = parentBottom - lp.bottomMargin;
                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(lp.gravity, width, height, mTempContainerRect, mTempChildRect);
                // Place the child.
                child.layout(mTempChildRect.left, mTempChildRect.top,
                        mTempChildRect.right, mTempChildRect.bottom);
            }
        }
    }


    public static class CustomLayoutParams extends MarginLayoutParams {
        public int gravity = Gravity.TOP | Gravity.START;
        public static int POSITION_MIDDLE = 0;
        public static int POSITION_LEFT = 1;
        public static int POSITION_RIGHT = 2;
        public int position = POSITION_MIDDLE;

        public CustomLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutParams);
            gravity = a.getInt(R.styleable.CustomLayoutParams_android_layout_gravity, gravity);
            position = a.getInt(R.styleable.CustomLayoutParams_layout_position, position);

            a.recycle();
        }

        public CustomLayoutParams(int width, int height) {
            super(width, height);
        }

    }
}
