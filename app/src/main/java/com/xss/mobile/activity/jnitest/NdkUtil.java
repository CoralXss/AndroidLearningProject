package com.xss.mobile.activity.jnitest;

/**
 * Created by xss on 2016/11/21.
 */
public class NdkUtil {
    static {
        System.loadLibrary("myJniTest");
    }

    // Java调用C方法
    public static native String getCLanguageString();

    // C调用Java方法,提供入口
    public static native void callJavaMethodFromJni();
}
