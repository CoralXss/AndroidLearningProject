package com.xss.mobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.renderscript.Type;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.xss.mobile.R;

/**
 * Created by xss on 2017/1/2.
 */

public class LeanTextView extends TextView {
    private static final int DEFAULT_DEGREE = 45;
    private int mDegrees = 0;

    public LeanTextView(Context context) {
        this(context, null);
    }

    public LeanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LeanTextView);

        mDegrees = ta.getDimensionPixelSize(R.styleable.LeanTextView_leanDegree, DEFAULT_DEGREE);

        ta.recycle();
    }

    public void setDegree(int degree) {
        this.mDegrees = degree;
        invalidate(); // 重新绘制

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        setMeasuredDimension((int) (getMeasuredWidth()),
                (int) (getMeasuredWidth()));

    }

    @Override
    protected void onDraw(Canvas canvas) {this.getPaint().measureText(getText().toString());
        canvas.save();

        canvas.translate(getCompoundPaddingLeft(), (float) (getHeight() * Math.sqrt(2) / 2));
        canvas.rotate(-45, getWidth() / 2f, getHeight() / 2f);

        super.onDraw(canvas);

        canvas.restore();
    }
}
