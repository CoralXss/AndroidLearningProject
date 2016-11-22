package com.xss.mobile.activity.bitmap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.xss.mobile.R;
import com.xss.mobile.fragment.ImageDetailFragment;
import com.xss.mobile.utils.bitmap.BitmapWorkerTask;

/**
 * Created by xss on 2016/11/3.
 * desc:
 * 1、可能存在的问题： 若加载小图片或者经缩放并缓存的图片不会有问题，
 *                      但是大图在主线程中直接加载会使UI卡顿不响应。
 * 2、解决方式：异步处理图片并增加缓存
 *             a.在Fragment中使用AsyncTask加载Bitmap图片；
 *             b.在每个Fragment中，若内存缓存中存在Bitmap就直接加载，
 *               若不存在就生成一个AsyncTask去加载图片。
 */
public class ViewPagerBitmapTestActivity extends FragmentActivity {
    public static final String EXTRA_IMAGE = "extra_image";
    private ViewPager viewPager;
    private ImagePagerAdapter imagePagerAdapter;

    private LruCache<String, Bitmap> mMemoryCache = null;

    public static final Integer[] imageResIds = new Integer[] {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_bitmap_test);

        initMemoryCache();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), imageResIds.length);
        viewPager.setAdapter(imagePagerAdapter);
    }

    /**
     * 初始化Bitmap所占内存缓存大小
     */
    private void initMemoryCache() {
        final int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 1/8的内存空间被用作缓存
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
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

    /**
     * 如果LruCache缓存中存在，直接显示到ImageView，不存在，就开启后台线程去处理显示图片
     * @param resId
     * @param imageView
     */
    public void loadBitmap(int resId, ImageView imageView) {
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = getBitmapFromMemoryCache(imageKey);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.add); // placeholder默认图片
            BitmapWorkerTask task = new BitmapWorkerTask(mMemoryCache, imageView, ViewPagerBitmapTestActivity.this);
            task.execute(resId);
        }

    }

    /**
     * 使用FragmentStatePagerAdapter 可以在ViewPager中的姿势图切换出屏幕时自动销毁与保存Fragments的状态，
     *      这样可以保持消耗更少的内存。
     * 注：若只使用为数不多的图片并且不会超出程序内存限制，使用PagerAdapter或FragmentPagerAdapter更加合适
     */
    public class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;
        private Integer[] imageResIds;

        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.mSize = size;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mSize;
        }
    }
}
