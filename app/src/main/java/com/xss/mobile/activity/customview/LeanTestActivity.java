package com.xss.mobile.activity.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.xss.mobile.R;

/**
 * Created by xss on 2017/1/3.
 */

public class LeanTestActivity extends Activity {
    private TextView tv_poster_type, tv_project_name, tv_project_price;
    private TextView tv_project_addr, tv_project_area, tv_project_open_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lean);

        tv_poster_type = (TextView) findViewById(R.id.tv_poster_type);
        tv_project_name = (TextView) findViewById(R.id.tv_project_name);
        tv_project_price = (TextView) findViewById(R.id.tv_project_price);

        startAnimation();

    }

    private void startAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(tv_poster_type, "rotation", 0f, -45f);
        animator.setDuration(300);
        animator.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(tv_project_name, "rotation", 0f, -45f);
        animator1.setDuration(300);
        animator1.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(tv_project_price, "rotation", 0f, -45f);
        animator2.setDuration(300);
        animator2.start();

//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(animator, animator1, animator2);
//        animatorSet.setDuration(1000);
//        animator.start();
    }
}
