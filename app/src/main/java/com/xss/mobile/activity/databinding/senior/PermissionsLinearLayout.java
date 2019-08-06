package com.xss.mobile.activity.databinding.senior;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by xss on 2017/10/30.
 */

public class PermissionsLinearLayout extends LinearLayout implements Permissions {

    private boolean permission = true;

    public PermissionsLinearLayout(Context context) {
        super(context);
    }

    public PermissionsLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PermissionsLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PermissionsLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !permission || super.dispatchTouchEvent(ev);
    }

    @Override
    public void setPermissionEnable(boolean enable, boolean inVisibility) {
        permission = enable;
    }
}
