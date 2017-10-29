package com.xss.mobile.activity.dagger;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by xss on 2017/7/29.
 */

public class LoadPresenter {
    String TAG = LoadPresenter.class.getSimpleName();

    private LoadView mLoadView;

//    @Inject
//    // 只能有一个构造防范可以添加 @Inject 注解，否则编译会报错
//    public LoadPresenter() {
//        Log.e(TAG, "no params constructor");
//    }

    // 2. 在 LoadPresenter构造方法中添加 @Inject 标记，
    @Inject
    public LoadPresenter(LoadView loadView) {
        Log.e(TAG, "one param constructor");
        this.mLoadView = loadView;
    }

    public void loadData() {
        Log.e(TAG, "--updateView()--");
        mLoadView.updateView();
    }

    public void test() {
        Log.e(TAG, "--test --");
    }
}
