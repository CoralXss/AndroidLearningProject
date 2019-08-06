package com.xss.mobile.activity.databinding.senior;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by xss on 2017/10/30.
 */

public class BindAdapter {

    @BindingAdapter({"permissions"})
    public static void setPermissions(View view, String permission) {
        boolean enable = PermissionCenter.hasPermission(permission);
        if (view instanceof Permissions) {
            ((Permissions) view).setPermissionEnable(enable, false);
        } else {
            setViewVisibleOrGone(view, enable);
        }
    }

//    @BindingAdapter({"permissions", "permissionDefault"})
//    public static void setPermissions(View view, String permissions, boolean permissionDefault) {
//
//    }

    public static void setViewVisibleOrGone(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
}
