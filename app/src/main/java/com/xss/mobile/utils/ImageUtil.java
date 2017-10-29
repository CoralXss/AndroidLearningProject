package com.xss.mobile.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by xss on 2017/1/10.
 * 关于图片剪切问题
 * 规则：
 * 1. 从相册截大图，使用uri
 */

public class ImageUtil {

    /**
     * 裁剪图片
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     * @param isReturnData
     */
    public static void startZoomPhoto(int aspectX, int aspectY, int outputX, int outputY, boolean isReturnData) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
    }

    /**
     * 将 bitmap转化为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] bytes = bos.toByteArray();

        String codeString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return codeString;

    }

    /**
     * 将base64转化为 bitmap
     * @param base64CodeString
     * @return
     */
    public static Bitmap base64ToBitmap(String base64CodeString) {
        byte[] bytes = Base64.decode(base64CodeString, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }
}
