package com.xss.mobile.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by xss on 2017/2/20.
 */

public class MyScrollListVIew extends ListView {
    private Paint mPaint;

    public MyScrollListVIew(Context context) {
        super(context);
    }

    public MyScrollListVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollListVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        RectF rectF = new RectF(100, 100, 800, 400);
        canvas.drawRoundRect(rectF, 30, 30, mPaint);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
