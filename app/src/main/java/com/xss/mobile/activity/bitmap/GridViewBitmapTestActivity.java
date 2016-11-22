package com.xss.mobile.activity.bitmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.xss.mobile.R;
import com.xss.mobile.utils.bitmap.BitmapWorkerTask;
import com.xss.mobile.widget.drawable.AsyncDrawable;

/**
 * Created by xss on 2016/11/4.
 * desc:
 * 1、可能存在的问题：
 *              a.若加载小图片或者经缩放并缓存的图片不会有问题，
 *                      但是大图在主线程中直接加载会使UI卡顿不响应。
 *              b.并发问题。GridView/ListView的ItemView在用户滑动屏幕时被循环使用，
 *                若每个子视图分发一个AsyncTask，就无法确保关联的视图在任务结束时，
 *                分配的视图已进入循环队列，给另外一个子视图进行重用。
 * 2、解决方式：1）异步处理图片并增加缓存：
 *             a.在Fragment中使用AsyncTask加载Bitmap图片；
 *             b.在每个Fragment中，若内存缓存中存在Bitmap就直接加载，
 *               若不存在就生成一个AsyncTask去加载图片。
 *             2）并发问题：
 *             a.创建一个专用的Drawable的子类存储任务的引用；
 *             b.在执行AsyncTask之前，创建一个AsyncDrawable并将它绑定到指定控件ImageView中；
 *             c.如果检查有另一个正在执行的任务同该ImageView关联起来了，取消另一个任务执行本任务。
 */
public class GridViewBitmapTestActivity extends Activity {
    private GridView gridView;
    private ImageGridAdapter imageGridAdapter;

    public static final Integer[] imageResIds = new Integer[] {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_bitmap_test);

        gridView = (GridView) findViewById(R.id.gridView);
        imageGridAdapter = new ImageGridAdapter(this);
        gridView.setAdapter(imageGridAdapter);
    }

    public void loadBitmap(int resId, ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView, this);
            // 占位图
            Bitmap mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(),
                    mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    /**
     * 检查是否有另一个正在执行的任务与该ImageView关联，若是，执行cancel()取消另一个任务。
     *
     * @param data
     * @param imageView
     * @return
     */
    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            int bitmapData = bitmapWorkerTask.data;
            if (bitmapData == 0 || bitmapData != data) {
                // 取消前一个task
                bitmapWorkerTask.cancel(true);
            } else {
                // 当前任务正在执行
                return false;
            }
        }
        // 没有任务关联该ImageView或者与该ImageView关联的前一个任务被取消了
        return true;
    }

    /**
     * 检测AsyncTask是否已经被分配到指定的ImageView
     * @param imageView
     * @return
     */
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

    private class ImageGridAdapter extends BaseAdapter {
        private Context mContext;

        public ImageGridAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return imageResIds.length;
        }

        @Override
        public Integer getItem(int i) {
            return imageResIds[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(
                        300,
                        300
                ));
            } else {
                imageView = (ImageView) convertView;
            }

            // 加载图片到ImageView
            loadBitmap(imageResIds[i], imageView);

            return imageView;
        }
    }
}
