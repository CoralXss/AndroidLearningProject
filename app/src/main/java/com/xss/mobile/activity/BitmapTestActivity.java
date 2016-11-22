package com.xss.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_test);

        imageView = (ImageView) findViewById(R.id.imageView);
//        loadBitmap(R.mipmap.ic_launcher, imageView);

        showDialog();
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
        showPopWindow();
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
