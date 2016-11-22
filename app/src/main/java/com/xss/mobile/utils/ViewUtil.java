package com.xss.mobile.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ViewUtil {
    private ViewUtil() {
    }


    /**
     * 适用于Adapter中简化ViewHolder相关代码
     *
     * @param convertView
     * @param id
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T obtainView(View convertView, int id) {
        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if (holder == null) {
            holder = new SparseArray<View>();
            convertView.setTag(holder);
        }
        View childView = holder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            holder.put(id, childView);
        }
        return (T) childView;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return height;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String autoSplit(String content, Paint p, float width) {
        int length = content.length();
        float textWidth = p.measureText(content);
        if (textWidth <= width) {
            return content;
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            if (p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return Arrays.toString(lineTexts);
    }

    /**
     * 半角换成全角
     *
     * @param input
     * @return
     */
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }


    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }

    /**
     * 获取缩略图
     *
     * @param url
     * @return
     */
    public static String getThumbImageUrl(String url, int w, int h) {

        if (TextUtils.isEmpty(url))
            return url;
        String thumbStr = "thumb/" + w + "M" + h;
        if (!url.contains("orig")) {
            thumbStr += "/orig";
        }
        String thumbUrl = url.replace("image", thumbStr);
        return thumbUrl;
    }

    public static void setDiffTextSize(Context ct, TextView tv, String txt1, String txt2, int style1, int style2) {
        String txt = txt1 + txt2;
        SpannableString styledText = new SpannableString(txt);
        styledText.setSpan(new TextAppearanceSpan(ct, style1), 0, txt1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(ct, style2), txt1.length(), txt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(styledText, TextView.BufferType.SPANNABLE);
    }


    /**
     * view设置background drawable
     *
     * @param view
     * @param drawable
     */
    @SuppressWarnings("deprecation")
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    /**
     * @param color
     * @return
     */
    public static Drawable getBgDrawable(String color) {
        int strokeWidth = 2; // 边框宽度
        int roundRadius = 4; // 圆角半径
        int strokeColor = Color.parseColor(color);//边框颜色
        int fillColor = Color.parseColor("#00000000");//内部填充颜色 (透明)

        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }


    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    public static int getAllListViewSectionCounts(ListView lv, List<?> dataSource) {
        if (null == lv || (dataSource != null && !dataSource.isEmpty())) {
            return 0;
        }
        return dataSource.size() + lv.getHeaderViewsCount() + lv.getFooterViewsCount();
    }

    /**
     * 使用ColorFilter来改变亮度
     *
     * @param imageview
     * @param brightness
     */
    public static void changeBrightness(ImageView imageview, float brightness) {
        imageview.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }

    public static void changeBrightness(Drawable drawable, float brightness) {
        drawable.setColorFilter(getBrightnessMatrixColorFilter(brightness));
    }

    private static ColorMatrixColorFilter getBrightnessMatrixColorFilter(float brightness) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(
                new float[]{
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });
        return new ColorMatrixColorFilter(matrix);
    }

}
