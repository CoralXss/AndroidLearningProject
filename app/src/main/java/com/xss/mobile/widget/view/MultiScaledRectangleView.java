package com.xss.mobile.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xss.mobile.utils.ViewUtil;

/**
 * Created by xss on 2017/2/21.
 */

public class MultiScaledRectangleView extends View {
    private Paint mPaint;
    private int mScreenWidth, mScreenHeight;

    public MultiScaledRectangleView(Context context) {
        super(context);
        init(context);
    }

    public MultiScaledRectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiScaledRectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScreenWidth = ViewUtil.getScreenWidth(context);
        mScreenHeight = ViewUtil.getScreenHeight(context);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        mPaint.setTextSize(20f);
        mPaint.setStrokeWidth(10f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 将坐标系原点移动到画布中心
        canvas.translate(mScreenWidth / 2, mScreenHeight / 2);

        // 图形一：绘制20个不断缩放的小矩形
//        // 矩形区域（移动画布，也即移动整个坐标轴到原点中心，所以绘制矩形时，以此时的原点中心为对角线焦点绘制矩形，所以 left=-400, top=-400）
//        RectF rectF = new RectF(-400, -400, 400, 400);
//        for (int i = 0; i < 10; i++) {
//            canvas.scale(0.9f, 0.9f);
//            canvas.drawRect(rectF, mPaint);
//        }


        // 图形二：绘制圆环
        canvas.drawCircle(0, 0, 400, mPaint);
        canvas.drawCircle(0, 0, 380, mPaint);
        // 绘制圆形之间的连线
        for (int i = 0; i <= 360; i+=10) {
            canvas.drawLine(0, 380, 0, 400, mPaint);
            canvas.rotate(10);
        }

    }
}
