package com.xss.mobile.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xss.mobile.R;

/**
 * Created by xss on 2017/1/3.
 */

public class LeanImageView extends ImageView {
    private Paint mPaint;
    private Xfermode mXfermode;
    private Bitmap mBitmapMask;

    public LeanImageView(Context context) {
        this(context, null);
    }

    public LeanImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        LabelView labelView = new LabelView(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        createMask();
    }

    private void createMask() {
        if (mBitmapMask == null) {
            int maskWidth = getMeasuredWidth();
            int maskHeight = getMeasuredHeight();
            mBitmapMask = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBitmapMask);
            canvas.translate(maskWidth / 2, 0);
            canvas.rotate(45);
            int rectSize = (int) (maskWidth / 2 / Math.sin(Math.toRadians(45)));
            canvas.drawRect(0, 0, rectSize, rectSize, mPaint);
        }
    }

    @Override
    public void invalidate() {
        if (mBitmapMask != null) {
            mBitmapMask.recycle();
        }

        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int id = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        // 关键方法
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(mBitmapMask, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(id);
    }
}
