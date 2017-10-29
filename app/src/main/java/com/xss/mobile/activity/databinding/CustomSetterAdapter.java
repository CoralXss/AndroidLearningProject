package com.xss.mobile.activity.databinding;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by xss on 2017/10/12.
 */

public class CustomSetterAdapter {

    /**
     * 当给View 使用 android:paddingLeft 属性时，该方法会被调用
     * <TextView android:paddingLeft="10dp" ... />
     *
     * View 没有提供 setPaddingLeft() 方法，只有 setPadding() 方法，四个属性一起设置，这样稍微好理解些
     *
     * @param view
     * @param padding
     */
    @BindingAdapter("android:paddingLeft")
    public static void setPaddingLeft(View view, int padding) {
        view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * <ImageView
     *      app:imageUrl="@{venue.imageUrl}"
            app:error="@{@drawable/venueError}"/>

      在布局中使用该属性，会调用以下 loadImage() 方法，这一步由DataBinding完成，不用再Java代码中手动设置。
     * @param view
     * @param imgUrl
     * @param errorImg
     */
//    @BindingAdapter({"bind:imageUrl", "bind:error"})
//    public static void loadImage(ImageView view, String imgUrl, Drawable errorImg) {
//        // Picasso.with(view.getContext()).load(url).error(errorImg).into(view);
//    }


    /**
     * 不用写成 @{visiblity == View.Visible ? View.VISIBLE : View.GONE}
     * 直接通过 BindingConversion 转换，简化代码
     * <View android:visibility="@{isVisible ? View.VISIBLE : View.GONE}" ... />
     * @param visibility
     * @return
     */
    @BindingConversion
    public static int visibility(boolean visibility) {
        return visibility ? View.VISIBLE : View.GONE;
    }

}
