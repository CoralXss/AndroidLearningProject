package com.xss.mobile;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xss.mobile.utils.DensityUtil;

public class Main2Activity extends AppCompatActivity {

    String TAG = Main2Activity.class.getSimpleName();

    private ScrollView sl_content;
    private LinearLayout ll_content;

    private LinearLayout ll_edit_native;
    private EditText edt_extra_info;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_guide_identification);

        sl_content = (ScrollView) findViewById(R.id.sl_content);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_edit_native = (LinearLayout) findViewById(R.id.ll_edit_native);
        edt_extra_info = (EditText) findViewById(R.id.edt_extra_info);
        print("onCreate");

        edt_extra_info.postDelayed(new Runnable() {
            @Override
            public void run() {
                print("createDelayed");

            }
        }, 100);

        edt_extra_info.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "foucse " + hasFocus );
                if (hasFocus) {

                }
            }
        });

        sl_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = ll_edit_native.getMeasuredHeight();

                int contentH = ll_content.getMeasuredHeight();

                if (isKeyboardShown(sl_content)) {
//                    sl_content.scrollTo(0, 900);
                    Log.e(TAG, "scroll " + scrollHeight + ", " + height + "， " + contentH) ;

                    // 滚动，若底部没有足够的控件滚动设置的值，就只会滚动能滚动的部分
                    sl_content.smoothScrollBy(0, scrollHeight);

//                    sl_content.scrollBy(0, scrollHeight - 300);
                } else {
                    ll_content.setPadding(0, 0, 0, 0);
                    sl_content.smoothScrollTo(0, 0);
                }
            }
        });
        // 361 - 760 滚动400

        sl_content.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 初始 839 = 键盘高度
                Log.e(TAG, scrollY + ", " + oldScrollY + ", " + (oldScrollY - scrollY));
            }
        });
    }

    private void print(String msg) {
        Log.e(msg, edt_extra_info.getMeasuredHeight() + ", " + edt_extra_info.getMeasuredWidth());
    }

    private int lastBottom = 0;
    private int scrollHeight = 0;
    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        // 根布局的可视区域
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        // getBottom()根布局本身底部坐标

        int heightDiff = rootView.getBottom() - r.bottom;
        Log.e(TAG, "bottom " + rootView.getBottom() + "， " + r.bottom + ", " + lastBottom);
//        return heightDiff > softKeyboardHeight * dm.density;

        boolean flag = false;
        int bottom = rootView.getBottom();
        if (lastBottom > bottom) {
            scrollHeight = Math.abs(bottom - lastBottom);  // + Math.abs(r.bottom - bottom);
            flag = true;
        }
        lastBottom = bottom;

        return flag;
    }

    @Override
    protected void onStart() {
        super.onStart();
        print("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        print("onResume");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        print("onAttachedToWindow");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        print("onWindowFocusChanged");
    }
}
