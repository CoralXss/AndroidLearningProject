//package com.xss.mobile.activity.jnitest;
//
//import android.util.Log;
//
///**
// * Created by xss on 2016/11/21.
// * desc: C调用Java方法
// * 如何在JNI中调用Java方法，首先需要了解 FindClass 和 GetMethodID。
// * jclass clz=(*env)->FindClass(env，"com/xss/mobile/JniHandle");
// * jmethodID getStringFromJava=(*env)->GetMethodID(env,class,"getStringForJava","()V");
// *     1、第四个参数是"()V"，为方法签名，因为java是支持重载的，所以需要标明函数的传参和返回值，来保证方法的唯一。
// *         其中 ()代表不传参数，V代表返回值为void。
// *     2、方法签名对于Java的参数都有一一对应的值；
// *     3、方法签名中用大写的字母对应了Java的基本数据类型。
// *
// */
//public class JniHandle {
//    /**
//     * C调用Java的方法，可理解为C需要获取UI线程中的某个参数，以此传参于C
//     * @return
//     */
//    public static String getStringFromStatic() {
//        Log.d("JniHandle", "c invoke Java");
//        return "String from static method in Java";
//    }
//}
