package com.xss.mobile.activity.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xss.mobile.R;
import com.xss.mobile.utils.AnimatorDrawUtils;

/**
 * Created by xss on 2016/11/5.
 * desc:简单加载动画
 */
public class ViewAnimationActivity extends Activity {
    private View mContentView;
    private View mLoadingView;
    private int shortAnimationDuration = 500;

    private View baobei_ring1;
    private View baobei_ring2;
    private View baobei_ring3;
    private View[] baobei_rings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);

        mContentView = findViewById(R.id.contentView);
        mLoadingView = findViewById(R.id.loading_spinner);

//        mContentView.setVisibility(View.GONE);
//        crossFade();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnimatorDrawUtils.myRingAnimator(baobei_rings);

//        scale();
//        scale1();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AnimatorDrawUtils.stopCustomerPageAnimator(baobei_rings);
    }

    public void initView() {
        baobei_rings = new View[3];
        baobei_ring1 = findViewById(R.id.v_ring_1);
        baobei_ring2 = findViewById(R.id.v_ring_2);
        baobei_ring3 = findViewById(R.id.v_ring_3);
        baobei_rings[0] = baobei_ring1;
        baobei_rings[1] = baobei_ring2;
        baobei_rings[2] = baobei_ring3;

//        startAnim();
    }

    int delay = 100;
    public void scale() {

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(baobei_ring1, "scaleX", 1.0f, 1.5f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(baobei_ring1, "scaleY", 1.0f, 1.5f);
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.setDuration(2000);
        animatorSet.setStartDelay(delay * (long) 0);
        animatorSet.start();
    }

    private void scale1() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(baobei_ring2, "scaleX", 1.0f, 1.5f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(baobei_ring2, "scaleY", 1.0f, 1.5f);
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.setDuration(2000);
        animatorSet.setStartDelay(delay * (long) 1);
        animatorSet.start();
    }

    private void startAnim() {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(baobei_ring1, "scaleX", 1.0f, 1.5f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(baobei_ring1, "scaleY", 1.0f, 1.5f);
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(baobei_ring1, "alpha", 1.0f, 0f);

        animatorSet.play(animatorX).with(animatorY).with(animatorA);
//
        animatorSet.setDuration(1000);
        animatorSet.start();
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
