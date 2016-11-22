package com.xss.mobile.activity.animation;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xss.mobile.R;

/**
 * Created by xss on 2016/11/5.
 * desc:Card翻转动画
 */
public class CardRotateAnimationActivity extends Activity {
    private View mContentView;
    private View mLoadingView;
    private int shortAnimationDuration = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);

        mContentView = findViewById(R.id.contentView);
        mLoadingView = findViewById(R.id.loading_spinner);

        mContentView.setVisibility(View.GONE);
        crossFade();
    }

    private void crossFade() {
        mContentView.setAlpha(0);
        mContentView.setVisibility(View.VISIBLE);

        mContentView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        mLoadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        // 即使透明度为0，也要为Gone。以免占空间
                        mLoadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }
}
