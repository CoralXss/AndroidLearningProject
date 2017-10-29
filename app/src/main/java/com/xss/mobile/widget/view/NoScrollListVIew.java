package com.xss.mobile.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by xss on 2017/2/21.
 */

public class NoScrollListVIew extends ListView {
    public NoScrollListVIew(Context context) {
        super(context);
    }

    public NoScrollListVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
