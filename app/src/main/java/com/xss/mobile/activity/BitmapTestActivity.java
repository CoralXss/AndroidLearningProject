package com.xss.mobile.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.xss.mobile.R;
import com.xss.mobile.utils.bitmap.BitmapWorkerTask;

/**
 * Created by xss on 2016/10/30.
 */
public class BitmapTestActivity extends Activity {
    private ImageView imageView;

    BitmapFactory.Options options;
    boolean isOnClicked = false;
    int inDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_test);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(getBitmap(R.drawable.welcome));

        options = new BitmapFactory.Options();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnClicked = !isOnClicked;

                imageView.setImageBitmap(getBitmap(R.drawable.welcome));
            }
        });
    }

    /**
     * 1080
     * @param resId
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Bitmap getBitmap(int resId) {
        options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
//        BitmapFactory.decodeStream(getResources().openRawResource(resId), null, options);
        Log.e("bit", options.outWidth + ", " + options.outHeight);

        options.inJustDecodeBounds = false;
//        Bitmap b = BitmapFactory.decodeResource(getResources(), resId, options);

        if (isOnClicked) {
            // 将inDensity设置为inTargetDensity，避免放大
            // 这里设置了 inDensity，若放在 xxhdpi，则 inDensity=480，若此时赋值为屏幕 dots-per-inch，1280x720手机，inDensity=320
            // 相当于
            options.inDensity = getResources().getDisplayMetrics().densityDpi;
        }

        // 设置越大，图片越不清晰，占用内存越小
        options.inSampleSize = 4;

        /**
         * 初始，未点击，opts.inDensity = 480，byte = 230400
         *        点击，opts.inDensity = 320, byte = 518400  上下byte比例 = 1.5 * 1.5 = 2.25
         *
         * 最终 bitmap.mHeight = 图片高 * 1/inSampleSize * inTarget/inDensity
         */


        Bitmap b = BitmapFactory.decodeResource(getResources(), resId, options); // BitmapFactory.decodeStream(getResources().openRawResource(resId), null, options);
        Log.e("byte", b.getByteCount() + ", " + b.getAllocationByteCount());

        return b;

    }

    private void showDialog() {
        new AlertDialog.Builder(this).setTitle("Hello").setMessage("This is a test").show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        showPopWindow();
    }

    private void showPopWindow() {
        PopupWindow pw = new PopupWindow(this);
        View v = View.inflate(this, R.layout.activity_binding, null);
        pw.setContentView(v);
        pw.setBackgroundDrawable(getDrawable(R.drawable.ageent_bg_ll_match_orange));
        pw.setHeight(300);
        pw.setWidth(300);
        pw.showAtLocation(imageView, Gravity.NO_GRAVITY, 0, 1);
    }

    // Caused by: android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?

    private void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, this);
        task.execute(resId);
    }
}
