package com.xss.mobile.activity.customview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xss.mobile.R;

public class FrameLayoutTestActivity extends Activity implements View.OnClickListener {
    private TextView tv_bottom, tv_center, tv_top;

    String TAG = FrameLayoutTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_layout_test);

        initView();
    }

    private void initView() {
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_center = (TextView) findViewById(R.id.tv_center);
        tv_top = (TextView) findViewById(R.id.tv_top);

        tv_bottom.setOnClickListener(this);
        tv_center.setOnClickListener(this);
        tv_top.setOnClickListener(this);

        setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onCLickView(View v) {
                onClick(tv_center);
                onClick(tv_top);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bottom:
                Log.e(TAG, "bottom : light blue");

//                onViewClickListener.onCLickView(v);

                break;
            case R.id.tv_center:
                Log.e(TAG, "center : light red");

                tv_bottom.callOnClick();

                break;
            case R.id.tv_top:
                Log.e(TAG, "top : light green");

                tv_center.callOnClick();

                break;
        }
    }

    private OnViewClickListener onViewClickListener;

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.onViewClickListener = listener;
    }

    interface OnViewClickListener {
        void onCLickView(View v);
    }
}
