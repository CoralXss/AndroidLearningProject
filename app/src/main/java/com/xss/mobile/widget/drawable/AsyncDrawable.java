package com.xss.mobile.widget.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.xss.mobile.utils.bitmap.BitmapWorkerTask;

import java.lang.ref.WeakReference;

/**
 * Created by xss on 2016/10/31.
 *
 * 可能会产生的并发问题：
 * 在用户滑动时ListView和GridView的Item视图会在用户滑动屏幕时被循环利用。
 *   每一个视图都触发一个AsyncTask，则无法确保关联的视图在结束任务时，
 *   分配的视图已进入循环队列中，给另外一个视图进行重用，
 *   并且无法确保所有的异步任务的完成顺序和他们本身的启动顺序保持一致。
 *
 * 解决方案：创建一个专用的Drawable子类来存储任务的引用。任务执行过程中，使用占位图。
 */
public class AsyncDrawable extends BitmapDrawable {

    private WeakReference bitmapWorkerTaskReference = null;

    public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
    }
}
