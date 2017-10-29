package com.xss.mobile.activity.dagger2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by xss on 2017/10/8.
 */

public abstract class BaseTestActivity<T, K extends BaseRecyclerAdapter> extends AppCompatActivity {
    protected K mAdapter;

//    public abstract T getMessage();

    public abstract K constructAdapter();

    public K getAdapter() {
        return mAdapter;
    }


}
