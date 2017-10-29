package com.xss.mobile.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.View;

/**
 * Created by hanxu on 2016/3/24.
 */
public class AnimatorDrawUtils {

    private static boolean find;
    private static int findType = 0;

    private static float viewBegin = 1.0f;
    private static float viewEnd = 1.3f;
    private static int viewDuration = 400;

    private static float viewRingsBegin = 1.0f;
    private static float viewRingsEnd = 5.0f;
    private static int viewRingsDuration = 2000;
    private static int viewRingsDelay = 500;

    private static float customerHeadsBegin = 0.0f;
    private static float customerHeadsEnd = 40.0f;

    private static int customerRingsDuration = 1000;
    private static int customerHeadsDuration = 1200;
    private static int customerHeadDelay = 500;

    private static AnimatorSet total;

    private static Handler mainHandler;
    /**
     * 意向客户动画组
     */
    private static AnimatorSet searchBefore;
    private static AnimatorSet[] searchings;
    private static Animator animator;
    private static AnimatorSet totalAnimatorSet;
    /**
     * 客户首页动画组
     */
    private static AnimatorSet baobeiTotal;
    private static AnimatorSet pushTotal;

    /**
     * 底部tip动画
     */
    private static AnimatorSet tipAnimator;

    public static int getFindType() {
        return findType;
    }

    public static void setFindType(int findType) {
        AnimatorDrawUtils.findType = findType;
    }

    public static boolean getFind() {
        return find;
    }

    public static void setFind(boolean find1) {
        find = find1;
    }

    public static AnimatorSet viewScaleChange(View view, float begin, float end, int duration) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", begin, end);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", begin, end);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY);
        animatorX.setDuration(duration);
        animatorY.setDuration(duration);
        animatorSet.setDuration(duration);
        return animatorSet;
    }

    public static AnimatorSet ringAnimations(View[] view, float begin, float end, int duration, int delay) {
        AnimatorSet animatorSetTotal = new AnimatorSet();
        for (int i = 0; i < view.length; i++) {
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(view[i], "scaleX", begin, end);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(view[i], "scaleY", begin, end);
            animatorSet.play(animatorX).with(animatorY);
            animatorX.setDuration(duration);
            animatorY.setDuration(duration);
            animatorSet.setDuration(duration);
            animatorSet.setStartDelay(delay * (long) i);
            animatorSetTotal.play(animatorSet);
        }
        return animatorSetTotal;
    }

    public static AnimatorSet[] myRingAnim(View[] view, float begin, float end, float beginA, float endA, int duration, int delay) {

        AnimatorSet[] scAnims = new AnimatorSet[view.length];

        float step = 0.3f;

        for (int i = 0; i < view.length; i++) {
            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator animatorX = ObjectAnimator.ofFloat(view[i], "scaleX", begin, end);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(view[i], "scaleY", begin, end);
            ObjectAnimator animatorA = ObjectAnimator.ofFloat(view[i], "alpha", beginA, endA - step * (i));

            animatorSet.play(animatorX).with(animatorY).with(animatorA);

            animatorSet.setDuration(duration);
            animatorSet.setStartDelay(delay * (long) i);

            scAnims[i] = animatorSet;
        }
        return scAnims;
    }


    public static AnimatorSet[] viewTransparencyChange(View[] view, float begin, float end, int duration, int delay) {
        AnimatorSet[] scAnims = new AnimatorSet[view.length];

        float step = 0.3f;
        for (int i = 0; i < view.length; i++) {

            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animatorA = ObjectAnimator.ofFloat(view[i], "alpha", begin, end - step * (i));
            animatorA.setDuration(duration);
            animatorSet.play(animatorA);
            animatorSet.setDuration(duration);
            animatorSet.setStartDelay(delay * (long) i);

            scAnims[i] = animatorSet;
        }
        return scAnims;
    }


    public static AnimatorSet viewTransparencyChange(View view, float begin, float end, int duration) {
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(view, "alpha", begin, end);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorA);
        animatorA.setDuration(duration);
        animatorSet.setDuration(duration);
        return animatorSet;
    }

    public static AnimatorSet viewPositionYChange(View view, int y, float start, float end, int duration) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "y", start * y, end * y);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorY);
        animatorY.setDuration(duration);
        animatorSet.setDuration(duration);
        return animatorSet;
    }

    public static AnimatorSet viewPositionXChange(View view, int x, float start, float end, int duration) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "x", start * x, end * x);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX);
        animatorX.setDuration(duration);
        animatorSet.setDuration(duration);
        return animatorSet;
    }

    /**
     * 进行搜索前的动画处理
     *
     * @param view     即将被处理的中间不动的view
     * @param begin    动画属性初始值
     * @param end      动画结束属性值
     * @param duration 动画时间
     * @return 动画集合
     */
    public static AnimatorSet viewSearchBefore(View view, float begin, float end, int duration) {
        AnimatorSet toatlAnimatorSet = new AnimatorSet();
        AnimatorSet animatorSerBrfore = viewScaleChange(view, begin, begin, duration);
        AnimatorSet fristAnimatorSer = viewScaleChange(view, begin, end, duration);
        AnimatorSet secondAnimatorSer = viewScaleChange(view, end, begin, duration);
        toatlAnimatorSet.play(animatorSerBrfore).before(fristAnimatorSer);
        toatlAnimatorSet.play(fristAnimatorSer).before(secondAnimatorSer);
        toatlAnimatorSet.setDuration(duration);
        return toatlAnimatorSet;
    }


    /**
     * 进行搜索时的动画处理
     *
     * @param viewRing 进行外扩的Views
     * @param begin    动画属性初始值
     * @param end      动画结束属性值
     * @param duration 动画时间
     * @return 动画集合
     */
    public static AnimatorSet viewSearching(View viewRing, float begin, float end, int duration) {
        viewRing.setVisibility(View.VISIBLE);
        AnimatorSet toatlAnimatorSet = new AnimatorSet();
        AnimatorSet seachAnimatorSerFrist = viewTransparencyChange(viewRing, begin, 0.0f, duration);
        AnimatorSet seachAnimatorSerBefore = viewScaleChange(viewRing, begin, end, duration);
        toatlAnimatorSet.play(seachAnimatorSerBefore).with(seachAnimatorSerFrist);
        toatlAnimatorSet.setDuration(duration);
        return toatlAnimatorSet;
    }

    public static void showViews(View[] views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(View[] views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static void stopAnimation() {
        if (searchBefore != null) {
            searchBefore.cancel();
        }

        for (Animator aanimator : searchings) {
            if (aanimator != null) {
                aanimator.cancel();
            }
        }

        if (animator != null) {
            animator.cancel();
        }

        if (totalAnimatorSet != null) {
            totalAnimatorSet.cancel();
        }
    }


    public static void myRingAnimator(View[] baobei_rings) {

        baobeiTotal = new AnimatorSet();

        AnimatorSet[] baobeiScale = myRingAnim(baobei_rings, 1.0f, 1.4f, 1.0f, 0f, 2000, 500);

        for (int i = 0; i < baobei_rings.length; i++) {
            baobeiTotal.play(baobeiScale[i]);
        }


        baobeiTotal.start();
        baobeiTotal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                baobeiTotal.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }



    public static void stopTipAnimator() {
        if (tipAnimator != null) {
            tipAnimator.removeAllListeners();
            tipAnimator.cancel();
            tipAnimator = null;
        }
    }
}
