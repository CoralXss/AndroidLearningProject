package com.xss.mobile.activity.databinding;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by xss on 2019/8/22.
 */
public class CustomAttrBinding {
    private static final String TAG = "CustomAttrBinding";

    @BindingAdapter("customText")
    public static void customText(TextView view, String customText) {
        Log.e(TAG, "customText = " + customText);
        view.setText(customText);
    }
}
