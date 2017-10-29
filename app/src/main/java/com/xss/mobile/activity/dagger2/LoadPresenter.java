package com.xss.mobile.activity.dagger2;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by xss on 2017/7/29.
 */

public class LoadPresenter {
    String TAG = LoadPresenter.class.getSimpleName();

    private LoadView mLoadView;

//    @Inject
    public LoadPresenter(LoadView loadView) {
        this.mLoadView = loadView;
    }

    public void loadData() {
        Log.e(TAG, "--updateView()--");
        mLoadView.updateView();
    }
}
