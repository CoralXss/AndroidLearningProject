package com.xss.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xss.mobile.R;
import com.xss.mobile.activity.bitmap.ViewPagerBitmapTestActivity;

/**
 * Created by xss on 2016/11/3.
 */
public class ImageDetailFragment extends Fragment {
    private static final String IMAGE_DATA_EXTRA = "resId";

    private int mImageNum;
    private ImageView mImageView;

    public static ImageDetailFragment newInstance(int imageNum) {
        ImageDetailFragment f = new ImageDetailFragment();
        Bundle b = new Bundle();
        b.putInt(IMAGE_DATA_EXTRA, imageNum);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageNum = getArguments() != null ? getArguments().getInt(IMAGE_DATA_EXTRA) : -1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int resId = ViewPagerBitmapTestActivity.imageResIds[mImageNum];

        if (getActivity() instanceof ViewPagerBitmapTestActivity) {
            // 加载图片
            ((ViewPagerBitmapTestActivity)getActivity()).loadBitmap(resId, mImageView);
        }
    }
}
