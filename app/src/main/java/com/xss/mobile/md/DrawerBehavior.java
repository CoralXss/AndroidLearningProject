package com.xss.mobile.md;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

/**
 * Created by xss on 2017/9/18.
 */

public class DrawerBehavior extends CoordinatorLayout.Behavior<TextView> {

    private int mFrameMaxHeight = 100;
    private int mStartY;

    // 提供以下构造，便于在 .xml 文件中解析并反射获取对象
    public DrawerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        if (mStartY == 0) {
            mStartY = (int) dependency.getY();
        }
        float percent = dependency.getY() / mStartY;
        child.setY(child.getHeight() * (1 - percent) - child.getHeight());

        return true;
    }
}
