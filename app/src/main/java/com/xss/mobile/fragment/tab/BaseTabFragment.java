package com.xss.mobile.fragment.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

/**
 * Created by xss on 2019/8/7.
 */
public class BaseTabFragment extends Fragment {

    private boolean isShown, shouldUpgrade, enableExecuteOnPause;

    private void log(String msg) {
        Log.e("BaseTabFragment", msg);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        log("onCreate: " + this.getClass().getSimpleName());

        isShown = true;
        enableExecuteOnPause = true;
        onShowPageBehaviour();
    }

    @Override
    public void onResume() {
        super.onResume();

        log("onResume: " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();

        log("onPause: " + this.getClass().getSimpleName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            onHidePageBehaviour();
        } else if (isShown) {
            onShowPageBehaviour();
        }

        isShown = !hidden;

        log("onHiddenChanged: " + this.getClass().getSimpleName() + ": hidden = " + hidden);
    }

    @Override
    public void onStop() {
        super.onStop();

        log("onStop: " + this.getClass().getSimpleName());
    }

    private void onShowPageBehaviour() {

    }

    private void onHidePageBehaviour() {

    }

    public void onReady(Bundle savedInstanceState) {

    }

    public void onPageHide() {

    }



}
