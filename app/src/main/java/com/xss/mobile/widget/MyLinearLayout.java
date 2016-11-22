package com.xss.mobile.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xss.mobile.utils.DensityUtil;

/**
 * Created by xss on 2016/10/14.
 */
public class MyLinearLayout extends LinearLayout {
    String TAG = "MyLinearLayout";

    public MyLinearLayout(Context context) {
        super(context);
        Log.e(TAG, "Constructor 1");
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.e(TAG, "Constructor 2");
    }

    // 其他构造均是调用此构造方法
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG, "Constructor 3");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.YELLOW);
//        setBackgroundColor(Color.YELLOW);
    }

    private int mPaddingLeft = 10;
    private int mPaddingRight = 10;
    private int mPaddingTop = 10;
    private int mPaddingBottom = 10;
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        // 取得子视图的布局参数
        final LinearLayout.LayoutParams lp = new LayoutParams(child.getLayoutParams());

        // 通过getChildMeasureSpec获取最终的宽高详细测量值
        // spec, padding, childDimension
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft + mPaddingRight, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, mPaddingTop + mPaddingBottom, lp.height);

        // 将计算好的宽高详细测量值传入measure方法，完成最后的测量
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * 以下为最简单的测量View大小的方法，需根据情况来测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private int totalWidth = 0;
    private int totalHeight = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);

//        int measureWidth = measureWidth(widthMeasureSpec);
//        int measureHeight = measureHeight(heightMeasureSpec);

        // 计算自定义的ViewGroup中所有子控件的大小
//        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            // 取得子视图的布局参数
            final LinearLayout.LayoutParams lp = new LayoutParams(child.getLayoutParams());

            // 通过getChildMeasureSpec获取最终的宽高详细测量值
            // spec, padding, childDimension
            int childWidthMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST), mPaddingLeft + mPaddingRight, lp.width);
            int childHeightMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST), mPaddingTop + mPaddingBottom, lp.height);

            // 将计算好的宽高详细测量值传入measure方法，完成最后的测量
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            totalHeight += child.getMeasuredHeight();
            totalWidth += child.getMeasuredWidth();
        }

        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
//        Log.e("ll", "width " + result);
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
//        Log.e("ll", "height " + result);
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0, top = 0, right = 0, bottom = 0;

        Log.e(TAG, "onLayout");

        // 0 201 300 501
//        Log.e("ll", "layout: " + l + ", " + t + ", " + r + ", " + b);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            left =  width * i + 10 * i;
            right = left + width;
            top =  height * i + 10 * i;
            bottom = top + height;

            child.layout(left, top, right, bottom);
        }
    }


    /**
     * 在measureChildren中最难的部分：找出传递给child的MeasureSpec。
     * 目的是结合父view的MeasureSpec与子view的LayoutParams信息去找到最好的结果
     * （也就是说子view的确切大小由两方面共同决定：1.父view的MeasureSpec 2.子view的LayoutParams属性）
     *
     * @param spec 父view的详细测量值(MeasureSpec)
     * @param padding view当前尺寸的的内边距和外边距(padding,margin)
     * @param childDimension child在当前尺寸下的布局参数宽高值(LayoutParam.width,height)
     */
    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        //父view的模式和大小
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        //通过父view计算出的子view = 父大小-边距（父要求的大小，但子view不一定用这个值）
        int size = Math.max(0, specSize - padding);

        //子view想要的实际大小和模式（需要计算）
        int resultSize = 0;
        int resultMode = 0;

        //通过1.父view的MeasureSpec 2.子view的LayoutParams属性这两点来确定子view的大小
        switch (specMode) {
            // 当父view的模式为EXACITY时，父view强加给子view确切的值
            case MeasureSpec.EXACTLY:
                // 当子view的LayoutParams>0也就是有确切的值
                if (childDimension >= 0) {
                    //子view大小为子自身所赋的值，模式大小为EXACTLY
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                    // 当子view的LayoutParams为MATCH_PARENT时(-1)
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    //子view大小为父view大小，模式为EXACTLY
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                    // 当子view的LayoutParams为WRAP_CONTENT时(-2)
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    //子view决定自己的大小，但最大不能超过父view，模式为AT_MOST
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // 当父view的模式为AT_MOST时，父view强加给子view一个最大的值。
            case MeasureSpec.AT_MOST:
                // 道理同上
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // 当父view的模式为UNSPECIFIED时，子view为想要的值
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // 子view大小为子自身所赋的值
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // 因为父view为UNSPECIFIED，所以MATCH_PARENT的话子类大小为0
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // 因为父view为UNSPECIFIED，所以WRAP_CONTENT的话子类大小为0
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }
}
