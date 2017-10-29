package com.xss.mobile.activity.databinding;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by xss on 2017/10/12.
 */

public class ClickBindEvent {

    public void onClickView(View view) {
        Log.e("Click", "点击了");
        Toast.makeText(view.getContext(), "绑定点击事件", Toast.LENGTH_SHORT).show();
    }
}
