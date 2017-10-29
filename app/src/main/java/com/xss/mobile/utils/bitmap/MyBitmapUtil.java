package com.xss.mobile.utils.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by xss on 2016/10/30.
 * 如何决定加载整张图片还是缩小版到内存？
 * 1、评估加载完整图片所耗内存；
 * 2、程序加载这张图片可能涉及到的其他内存需求（？？？）；
 * 3、呈现图片的控件尺寸大小；（图片应同控件大小匹配）
 * 4、屏幕大小与当前设备的屏幕密度。（加载分辨率超过屏幕分辨率的图片会占内存）
 */
public class MyBitmapUtil {
    private static final String TAG = "MyBitmapUtil";

    /**
     * 根据目标图片大小计算Sample图片大小
     * Example: 一张2048x1536的图片，若inSampleSize=4，则会得到一张512x384的Bitmap(ARGB_8888)，
     *  加载缩小的图片大概占内存(512*384*4byte / (1024*1024))=0.75MB，加载完整尺寸会占12MB内存。
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 读取加载图片的尺寸
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // inSampleSize为2的幂，解码器最终会对非2的幂的数进行向下处理
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampleBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置为true时在解码时可避免内存分配，会返回一个null的bitmap，但是可以此得到图片尺寸和类型
        options.inJustDecodeBounds = true;
        Bitmap bmBefore = BitmapFactory.decodeResource(resources, resId, options);
        Log.d(TAG, "inJustDecodeBounds = true, bitmap = " + (null == bmBefore));

        // 缩小图片
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }
}
