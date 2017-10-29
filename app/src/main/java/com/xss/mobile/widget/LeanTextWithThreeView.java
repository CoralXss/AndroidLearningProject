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
import android.view.View;

import com.xss.mobile.R;

/**
 * Created by xss on 2017/1/3.
 */

public class LeanTextWithThreeView extends View {
    private final static  String TAG = LeanTextWithThreeView.class.getSimpleName();

    private static final float SQRT_2 = (float) Math.sqrt(2);

    public static final int VISIBLE = 0;
    public static final int GONE = 1;

    public static final int DRAW_TOP = 0;
    public static final int DRAW_CENTER = 1;
    public static final int DRAW_BOTTOM = 2;

    private Paint mPaint;
    private Paint mTextPaint, mTextPaint2, mTextPaint3;
    private Rect mBound, mBound2, mBound3;

    private CharSequence mText = "", mText2 = "", mText3 = "";
    private float mTextSize, mTextSize2, mTextSize3;
    private int mTextColor, mTextColor2, mTextColor3;

    private int leanTextNum = 1;  // 倾斜文本个数
    private int leanTextVisible, leanText2Visible, leanText3Visible;

    private int leanSpan;
    private int leanSpan1 = 60;  // 自定义距离 view1和view2斜线的间距
    private int leanSpan2 = 40;  // line 2 和 3的间距

    private int DEFAULT_TEXT_SIZE = 20;

    public LeanTextWithThreeView(Context context) {
        this(context, null);
    }

    public LeanTextWithThreeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();

        init(context, attrs);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ffffff"));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#ffffff"));
        mTextPaint.setTextSize(mTextSize);

        mTextPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint2.setColor(Color.parseColor("#ffffff"));
        mTextPaint2.setTextSize(mTextSize2);

        mTextPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint3.setColor(Color.parseColor("#ffffff"));
        mTextPaint3.setTextSize(mTextSize3);

        mBound = new Rect();
        mBound2 = new Rect();
        mBound3 = new Rect();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LeanTextWithThreeView);

        mText = ta.getText(R.styleable.LeanTextWithThreeView_leanText);
        mTextColor = ta.getColor(R.styleable.LeanTextWithThreeView_leanTextColor, Color.parseColor("#ffffff"));
        mTextSize = ta.getDimensionPixelSize(R.styleable.LeanTextWithThreeView_leanTextSize, DEFAULT_TEXT_SIZE);

        mText2 = ta.getText(R.styleable.LeanTextWithThreeView_leanText2);
        mTextColor2 = ta.getColor(R.styleable.LeanTextWithThreeView_leanTextColor2, Color.parseColor("#ffffff"));
        mTextSize2 = ta.getDimensionPixelSize(R.styleable.LeanTextWithThreeView_leanTextSize2, DEFAULT_TEXT_SIZE);

        mText3 = ta.getText(R.styleable.LeanTextWithThreeView_leanText3);
        mTextColor3 = ta.getColor(R.styleable.LeanTextWithThreeView_leanTextColor3, Color.parseColor("#ffffff"));
        mTextSize3 = ta.getDimensionPixelSize(R.styleable.LeanTextWithThreeView_leanTextSize3, DEFAULT_TEXT_SIZE);

        // 斜线之间的距离
        leanSpan = ta.getDimensionPixelOffset(R.styleable.LeanTextWithThreeView_leanSpan, 0);
        leanSpan1 = ta.getDimensionPixelSize(R.styleable.LeanTextWithThreeView_leanSpan1, 0);
        leanSpan2 = ta.getDimensionPixelSize(R.styleable.LeanTextWithThreeView_leanSpan2, 0);

        leanTextNum = ta.getInt(R.styleable.LeanTextWithThreeView_leanTextNum, 1);
        // 可见性
        leanTextVisible = ta.getInt(R.styleable.LeanTextWithThreeView_leanTextVisible, 0);
        leanText2Visible = ta.getInt(R.styleable.LeanTextWithThreeView_leanText2Visible, 0);
        leanText3Visible = ta.getInt(R.styleable.LeanTextWithThreeView_leanText3Visible, 0);

        ta.recycle();
    }

    private int getMaxWidth(int w1, int w2, int w3) {
        int w = (w1 > w2) ? w1 : w2;
        return (w > w3) ? w : w3;
    }

    private void setPaintAndBound(String text, float textSize, Paint paint, Rect bound) {
        String txt = TextUtils.isEmpty(text) ? "" : text.toString();
        paint.setTextSize(textSize);
        paint.getTextBounds(txt, 0, txt.length(), bound);
    }

    private void setBoundsByVisibility() {
        mBound.setEmpty();
        mBound2.setEmpty();
        mBound3.setEmpty();

        if (leanTextVisible == VISIBLE) {
            mTextPaint.getTextBounds(mText.toString(), 0, mText.toString().length(), mBound);
            mTextPaint.setTextSize(mTextSize);

            if (leanText2Visible == VISIBLE) {
                mTextPaint2.getTextBounds(mText2.toString(), 0, mText2.toString().length(), mBound2);
                mTextPaint2.setTextSize(mTextSize2);

                if (leanText3Visible == VISIBLE) {
                    mTextPaint3.getTextBounds(mText3.toString(), 0, mText3.toString().length(), mBound3);
                    mTextPaint3.setTextSize(mTextSize3);
                }

            } else {
                mText2 = mText3;
                String txt = TextUtils.isEmpty(mText3) ? "" : mText3.toString();
                mTextPaint2.getTextBounds(txt, 0, txt.length(), mBound2);
                mTextPaint2.setColor(mTextColor2);
                mTextPaint2.setTextSize(mTextSize2);
            }

        } else {
            leanSpan = 0;
            if (leanText2Visible == VISIBLE) {
                // 将第二个设置为第一个文本
                leanSpan1 = 0;
                mText = mText2;
                mTextPaint.getTextBounds(mText2.toString(), 0, mText2.toString().length(), mBound);
                mTextPaint.setColor(mTextColor2);
                mTextPaint.setTextSize(mTextSize2);

                if (leanText3Visible == VISIBLE) {
                    mText2 = mText3;
                    String txt = TextUtils.isEmpty(mText3) ? "" : mText3.toString();
                    mTextPaint2.getTextBounds(txt, 0, txt.length(), mBound2);
                    mTextPaint2.setColor(mTextColor2);
                    mTextPaint2.setTextSize(mTextSize2);
                }

            } else {
                // 将第三个设置为第一个文本
                leanSpan1 = 0;
                leanSpan2 = 0;
                mText = mText3;
                String txt = TextUtils.isEmpty(mText3) ? "" : mText3.toString();
                mTextPaint.getTextBounds(txt, 0, txt.length(), mBound);
                mTextPaint.setColor(mTextColor3);
                mTextPaint.setTextSize(mTextSize3);
            }
        }
    }

    private void setBound(int pos) {
        switch (pos) {
            case 0:
                mBound.setEmpty();
                if (!TextUtils.isEmpty(mText)) {
                    mTextPaint.getTextBounds(mText.toString(), 0, mText.toString().length(), mBound);
                    mTextPaint.setColor(mTextColor);
                    mTextPaint.setTextSize(mTextSize);
                }
                break;
            case 1:
                mBound2.setEmpty();
                if (!TextUtils.isEmpty(mText2)) {
                    mTextPaint2.getTextBounds(mText2.toString(), 0, mText2.toString().length(), mBound2);
                    mTextPaint2.setColor(mTextColor2);
                    mTextPaint2.setTextSize(mTextSize2);
                }
                break;
            case 2:
                mBound3.setEmpty();
                if (!TextUtils.isEmpty(mText3)) {
                    mTextPaint3.getTextBounds(mText3.toString(), 0, mText3.toString().length(), mBound3);
                    mTextPaint3.setColor(mTextColor3);
                    mTextPaint3.setTextSize(mTextSize3);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        int span = leanSpan + leanSpan1 + mBound.height() + leanSpan2 + mBound2.height() + mBound3.height();

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            setBound(DRAW_TOP);
            setBound(DRAW_CENTER);
            setBound(DRAW_BOTTOM);

            float w1 = mBound.width() * SQRT_2 / 2 + (span * SQRT_2 / 2);
            float w2 = mBound2.width() * SQRT_2 / 2 + (leanSpan2 + mBound2.height()) * SQRT_2 / 2;
            float w3 = mBound3.width() * SQRT_2 / 2;

            width = (int) (getMaxWidth((int)w1, (int)w2, (int)w3) + (span * SQRT_2 / 2));

        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {

            float h1 = mBound.width() * SQRT_2 / 2;
            float h2 = mBound2.width() * SQRT_2 / 2 + (leanSpan1 + mBound.height()) * SQRT_2 / 2;
            float h3 = mBound3.width() * SQRT_2 / 2 + (span * SQRT_2 / 2);

            height = (int) (getMaxWidth((int)h1, (int)h2, (int)h3) + (span * SQRT_2 / 2));
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBound(DRAW_TOP);
        setBound(DRAW_CENTER);
        setBound(DRAW_BOTTOM);

//        if (!TextUtils.isEmpty(mText)) {
            drawTop(canvas);
//        }

//        if (!TextUtils.isEmpty(mText2)) {
            drawCenter(canvas);
//        }

//        if (!TextUtils.isEmpty(mText3)) {
            drawBottom(canvas);
//        }
    }

    /**
     * 绘制第一个文本
     * @param canvas
     */
    private void drawTop(Canvas canvas) {
        float x = SQRT_2 * mBound.width() / 2;
        float y = leanSpan * SQRT_2;

        Path path = getPath(canvas, 0f, x + y, x + y, 0f);

        float beginOffset = 0f;           // 字从哪个地方开始显示，居中=斜线的长度/2 - 字长/2
        float hOffset = mBound.height();
        String text = TextUtils.isEmpty(mText) ? "" : mText.toString();
        Paint p = mTextPaint;

        canvas.drawTextOnPath(text, path, beginOffset, hOffset, p);
    }

    /**
     * 绘制第二个文本
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        float x = (leanSpan + leanSpan1 + mBound.height()) * SQRT_2 + SQRT_2 * mBound.width() / 2;

        Path path = getPath(canvas, 0f, x, x, 0f);

        float beginOffset = leanSpan1 + mBound.height();   // 字从哪个地方开始显示=斜线的长度/2 - 字长/2
        float hOffset = mBound2.height();  // 可自定义
        String text = TextUtils.isEmpty(mText2) ? "" : mText2.toString();
        canvas.drawTextOnPath(text, path, beginOffset, hOffset, mTextPaint2);
    }

    /**
     * 绘制第三个文本
     * @param canvas
     */
    private void drawBottom(Canvas canvas) {
        float x = (leanSpan + leanSpan1 + mBound.height() + leanSpan2 + mBound2.height()) * SQRT_2 + SQRT_2 * mBound.width() / 2;

        // 1. 根据字体的长度画斜线
        Path path = getPath(canvas, 0f, x, x, 0f);

        float beginOffset = leanSpan1 + mBound.height() + leanSpan2 + mBound2.height();   // 字从哪个地方开始显示=斜线的长度/2 - 字长/2
        float hOffset = mBound3.height();  // 可自定义
        String text = TextUtils.isEmpty(mText3) ? "" :mText3.toString();
        canvas.drawTextOnPath(text, path, beginOffset, hOffset, mTextPaint3);
    }

    /**
     * 获取绘制文本的路径
     * @param canvas
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    private Path getPath(Canvas canvas, float startX, float startY, float endX, float endY) {
        Path path = new Path();
        path.reset();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
        path.close();
        canvas.drawPath(path, mPaint);

        return path;
    }

    /**
     * 设置三个Label的文本：从第一个开始设置，不越位设置文本
     * @param text
     */
    public void setText(String...text) {
        if (text.length == 0 || text.length > 3) {
            return;
        }
        if (text.length == 1) {
            mText = text[0];
        }

        if (text.length == 2) {
            mText = text[0];
            mText2 = text[1];
        }
        if (text.length == 3) {
            mText = text[0];
            mText2 = text[1];
            mText3 = text[2];
        }

        invalidate();
    }
}
