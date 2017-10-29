package com.xss.mobile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.xss.mobile.R;

/**
 * Created by xss on 2017/1/2.
 */

public class LeanLabelTextView extends View {
    private static final float SQRT_2 = (float) Math.sqrt(2);
    public static final int CENTER = 1;
    public static final int LEFT = 0;

    private String TAG = "Leantext";

    private Paint mPaint, mTextPaint;
    private Rect mBound;

    private CharSequence mText = "";
    private float mTextSize;
    private int mTextColor;

    private int mPaddingLeft, mPaddingRight;

    private int mGravity;

    public LeanLabelTextView(Context context) {
        this(context, null);
    }

    public LeanLabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 设置 path路径的 distance
        initPaint();

        init(context, attrs);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ffffff"));  //#00000000

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#ffffff"));

        mBound = new Rect();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LeanLabelTextView);

        mText = ta.getText(R.styleable.LeanLabelTextView_leanLabelText);
        mTextColor = ta.getColor(R.styleable.LeanLabelTextView_leanLabelTextColor, Color.parseColor("#ffffff"));
        mTextSize = ta.getDimensionPixelSize(R.styleable.LeanLabelTextView_leanLabelTextSize, 20);

        mGravity = ta.getInt(R.styleable.LeanLabelTextView_leanLabelTextGravity, 0);

//        // 斜体字距离顶点的位置
        mPaddingLeft = ta.getDimensionPixelSize(R.styleable.LeanLabelTextView_leanPaddingLeft, 0);
        mPaddingRight = ta.getDimensionPixelSize(R.styleable.LeanLabelTextView_leanPaddingRight, 0);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {

            String text = TextUtils.isEmpty(mText) ? "" : mText.toString();
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.getTextBounds(text, 0, text.length(), mBound);

            width = (int) (0 * 2 + mBound.width());  // * SQRT_2 / 2);
//            width = mBound.width() + 2 * mBound.height();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = width;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String text = TextUtils.isEmpty(mText) ? "" : mText.toString();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(text, 0, text.length(), mBound);

        float span = getMeasuredWidth();  // hOffset * SQRT_2 + lineLen
        float startX = 0f;      // mMarginLeft;
        float startY = span;   // mMarginTop + lineLen;
        float endX = span;
        float endY = 0f;


        // 1. 根据字体的长度画斜线
        Path path = new Path();
        path.reset();
        path.moveTo(startX, startY);
        path.lineTo(startX, startY + 0.5f);
        path.lineTo(endX + 0.5f, endY);
        path.lineTo(endX, endY);
        path.close();
        canvas.drawPath(path, mPaint);

        float beginOffset = mPaddingLeft;
        if (mGravity == CENTER) {
            beginOffset = endX * (float)Math.sqrt(2) / 2 - mBound.width() / 2;   // 字从哪个地方开始显示=斜线的长度/2 - 字长/2
        }
        float hOffset = 0;  // 可自定义
        canvas.drawTextOnPath(text, path, beginOffset, hOffset, mTextPaint);

    }

}
