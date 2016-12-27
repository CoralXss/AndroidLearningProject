package com.xss.mobile.widget.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xss on 2016/12/1.
 * desc: View 测量代码
 * MeasureSpec 一旦确定后，就可以在 onMeasure中确定 View的测量宽/高
 * MeasureSpec 是一个32位的 int 值，高2位代表 测量模式SpecMode，低30位表示 某种测量模式下的规格大小SpecSize
 */
public class ViewMeasureCode {

    /**
     * 类型一：对于 DecorView，其 MeasureSpec 由窗口的尺寸和其自身的 LayoutParams共同确定
     * @param windowSize
     * @param rootDimension
     * @return
     */
    private static int getRootMeasureSpec(int windowSize, int rootDimension) {
        int measureSpec;
        switch (rootDimension) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                // 设置rootView大小为window窗体的size大小
                measureSpec = View.MeasureSpec.makeMeasureSpec(windowSize, View.MeasureSpec.EXACTLY);
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                measureSpec = View.MeasureSpec.makeMeasureSpec(windowSize, View.MeasureSpec.AT_MOST);
                break;
            default:
                // window的size需要一个精确大小，
                measureSpec = View.MeasureSpec.makeMeasureSpec(rootDimension, View.MeasureSpec.EXACTLY);
                break;
        }
        return measureSpec;
    }

    /**
     * 在ViewGroup中获取childView的 MeasureSpec，由父容器的MeasureSpec结合自身的LayoutParams 来决定
     * @param parentSpec
     * @param padding       父容器中已占有的空间大小
     * @param childDimension
     * @return
     */
    private static int getChildMeasureSpec(int parentSpec, int padding, int childDimension) {
        int parentSpecMode = View.MeasureSpec.getMode(parentSpec);

        int size = Math.max(0, parentSpec - padding); // 子View可用大小 = 父容器大小 - padding

        int resultSize = 0;
        int resultMode = View.MeasureSpec.UNSPECIFIED;

        switch (parentSpecMode) {
            case View.MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    // 固定值为自己xml中设置的大小
                    resultSize = childDimension;
                    resultMode = View.MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = View.MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    // 子View自己决定大小，但是这个size不能超过父布局大小
                    resultSize = size;
                    resultMode = View.MeasureSpec.AT_MOST;
                }
                break;

            case View.MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = View.MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = View.MeasureSpec.AT_MOST;
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    // 子View自己决定大小，但是这个size不能超过父布局大小
                    resultSize = size;
                    resultMode = View.MeasureSpec.AT_MOST;
                }
                break;

            case View.MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = View.MeasureSpec.EXACTLY;
                } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
                    resultSize = 0;
                    resultMode = View.MeasureSpec.UNSPECIFIED;
                } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    resultSize = 0;
                    resultMode = View.MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }
}
