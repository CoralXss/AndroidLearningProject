package com.xss.mobile.activity.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.utils.AnimatorDrawUtils;
import com.xss.mobile.utils.DensityUtil;

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

    private TextView tv_content;

    private int startY;
    private float startScale;

    private boolean isScaled;

    String TAG = "Anim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);

        LinearLayout ll_messages = (LinearLayout) findViewById(R.id.ll_messages);
        tv_content = (TextView) findViewById(R.id.tv_content);
        int[] loc = new int[2];
        tv_content.getLocationOnScreen(loc);
        startY = loc[1];
        startScale = 1.0f;

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
//        baobei_rings = new View[3];
//        baobei_ring1 = findViewById(R.id.v_ring_1);
//        baobei_ring2 = findViewById(R.id.v_ring_2);
//        baobei_ring3 = findViewById(R.id.v_ring_3);
//        baobei_rings[0] = baobei_ring1;
//        baobei_rings[1] = baobei_ring2;
//        baobei_rings[2] = baobei_ring3;

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
        final View ll_search = findViewById(R.id.view_bg);
        findViewById(R.id.tv_click_to_scale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startY -= 20;
//                startScale *= 0.9f;
//                ViewCompat.setTranslationY(tv_content, startY);
//                ViewCompat.setScaleX(tv_content, startScale);

                isScaled = !isScaled;
                if (isScaled) {
                    ViewCompat.setScaleX(ll_search, 0.8f);
                } else {
                    ViewCompat.setScaleX(ll_search, 1f);
                }
            }
        });


        final View ll_translate = findViewById(R.id.ll_translate);
        final TextView tv_content = (TextView) findViewById(R.id.tv_content);
        final TextView tv_content0 = (TextView) findViewById(R.id.tv_content_0);
        Log.e(TAG, "translate: " + tv_content.getTranslationY() + ", " + ll_translate.getTranslationY());
        findViewById(R.id.tv_click_to_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startY -= 20;
                ViewCompat.setTranslationY(ll_translate, startY);
                // 所有的子View 都需要一起移动才行
//                ViewCompat.setTranslationY(tv_content, startY);
//                ViewCompat.setTranslationY(tv_content0, startY);

                Log.e(TAG, "translate2: " + tv_content.getTranslationY() + ", " + ll_translate.getTranslationY());
            }
        });




//        for (int i = 0; i < 5; i++) {
//            TextView tv = new TextView(this);
//            tv.setText("message " + i);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this, 18));
//            ll_messages.addView(tv, lp);
//        }
//
//        int height = ll_messages.getMeasuredHeight() == 0 ? DensityUtil.dip2px(this, 18) * 5 :
//                ll_messages.getMeasuredHeight();
//        int loc[] = new int[2];
//        ll_messages.getLocationOnScreen(loc);
//
////        ViewCompat.setTranslationY(ll_messages, height);
//
//        ll_messages.animate().translationY(height);


//        mContentView = findViewById(R.id.contentView);
//        mLoadingView = findViewById(R.id.loading_spinner);
//
//        mContentView.setVisibility(View.GONE);
//        crossFade();
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
