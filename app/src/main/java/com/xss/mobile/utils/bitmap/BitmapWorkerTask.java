package com.xss.mobile.utils.bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.xss.mobile.widget.drawable.AsyncDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by xss on 2016/10/30.
 * desc: 在本类中做一些如重设图片大小、从网络拉去图片的任务，可以确保不会阻塞UI线程.
 *  若后台啊线程不仅是一个简单的加载操作，增加一个内存缓存或磁盘缓存较好。
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Integer, Bitmap> {
    private final WeakReference imageViewReference;
    public int data = 0;
    private Activity activity;  // 此处也要注意内存泄露

    private LruCache<String, Bitmap> mMemoryCache;

    public BitmapWorkerTask(ImageView imageView, Activity activity) {
        // 使用弱引用确保AsyncTask所引用的资源imageView能被GC回收
        imageViewReference = new WeakReference(imageView);
        this.activity = activity;
    }

    public BitmapWorkerTask(LruCache<String, Bitmap> mMemoryCache, ImageView imageView, Activity activity) {
        // 使用弱引用确保AsyncTask所引用的资源imageView能被GC回收
        imageViewReference = new WeakReference(imageView);
        this.activity = activity;
        this.mMemoryCache = mMemoryCache;
    }

    // 如果缓存中不存在bitmap，就添加到缓存中
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    @Override
    protected Bitmap doInBackground(Integer...params) {
        data = params[0];
        Bitmap bitmap = MyBitmapUtil.decodeSampleBitmapFromResource(activity.getResources(),
                data, 100, 100);
        if (mMemoryCache != null) {
            // 把解析好的Bitmap添加到内存缓存中
            addBitmapToMemoryCache(String.valueOf(data), bitmap);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        // 当任务结束后不能确保ImageView是否仍存在，需要判断
        // 在任务结束前，用户回退或者旋转屏幕，可能使ImageView不存在
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = (ImageView) imageViewReference.get();

            BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (null == bitmapWorkerTask) {
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
