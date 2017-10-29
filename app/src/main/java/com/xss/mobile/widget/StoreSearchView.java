package com.xss.mobile.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xss.mobile.R;

/**
 * 店铺内搜索和排序View
 */
public class StoreSearchView extends LinearLayout {

    private TextView searchView;

    public StoreSearchView(Context context) {
        this(context, null);
    }

    public StoreSearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoreSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.re_store_search, this);
        setGravity(Gravity.CENTER_HORIZONTAL);

        searchView = (TextView) findViewById(R.id.search);

        searchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
