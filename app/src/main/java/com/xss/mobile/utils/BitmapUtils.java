package com.xss.mobile.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BitmapUtils {
    public static final int PREVIEW_SIZE = 230;

    public static boolean isValid(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    public static void release(HashMap<String, Bitmap> bitmaps) {
        Iterator<Map.Entry<String, Bitmap>> iter = bitmaps.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Bitmap> entry = iter.next();
            recycle(entry.getValue());
        }
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static Bitmap cropBitmap(Bitmap bitmap, float radio) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float r = (float) width / (float) height;
        if (r < radio) {
            int destH = (int) (width / radio);
            int delta = (int) ((height - destH) / 2f);
            return Bitmap.createBitmap(bitmap, 0, delta, width, height
                    - (2 * delta), null, true);
        } else {
            int destW = (int) (height * radio);
            int delta = (int) ((width - destW) / 2f);
            return Bitmap.createBitmap(bitmap, delta, 0, width - (2 * delta),
                    height, null, true);
        }
    }

    public static Bitmap loadBitmap(AssetManager am, String path) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inDither = false;
        option.inPurgeable = true;
        option.inInputShareable = true;
        option.inTempStorage = new byte[32 * 1024];
        option.inPreferredConfig = Bitmap.Config.ARGB_8888;

        InputStream in = null;
        try {
            in = am.open(path);
            return BitmapFactory.decodeStream(in, null, option);
        } catch (IOException e) {
            return null;
        } finally {
//            IOUtils.closeQuietly(in);
        }
    }

    public static Bitmap loadBitmap(String uri, int reqSize) {
        return loadBitmap(uri, reqSize, Bitmap.Config.RGB_565);
    }

    public static Bitmap loadBitmap(String uri, int reqSize,
                                    Bitmap.Config config) {
        if (TextUtils.isEmpty(uri)) {
            return null;
        }

        String realUrl = uri;
        if (uri.startsWith("file://")) {
            realUrl = uri.substring(6, uri.length());
        }

        int orientation = 0;
        try {
            ExifInterface exif = new ExifInterface(realUrl);
            if (exif != null) {
                int orien = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orien) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        orientation = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        orientation = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        orientation = 180;
                        break;
                }
            }
        } catch (IOException e) {

        }

        FileInputStream fis = null;
        FileDescriptor fd = null;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inDither = false;
        option.inPurgeable = true;
        option.inInputShareable = true;
        option.inTempStorage = new byte[32 * 1024];
        option.inPreferredConfig = config;

        try {
            fis = new FileInputStream(new File(realUrl));
            fd = fis.getFD();

            option.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, option);
            option.inSampleSize = caclulateInSampleSize(option, reqSize,
                    reqSize);
            option.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory
                    .decodeFileDescriptor(fd, null, option);
            if (orientation > 0) {
                Matrix matrix = new Matrix();
                matrix.setRotate(orientation, bitmap.getWidth() / 2,
                        bitmap.getHeight() / 2);
                Bitmap dest = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (dest != bitmap) {
                    bitmap.recycle();
                }
                return dest;
            } else {
                return bitmap;
            }
        } catch (IOException e) {

        } finally {
//            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    public static Bitmap loadBitmap(InputStream is, int reqSize,
                                    Bitmap.Config config) {
        try {
            FileInputStream fis = (FileInputStream) is;

            FileDescriptor fd = fis.getFD();

            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inDither = false;
            option.inPurgeable = true;
            option.inInputShareable = true;
            option.inTempStorage = new byte[32 * 1024];
            option.inPreferredConfig = config;

            option.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, option);
            option.inSampleSize = caclulateInSampleSize(option, reqSize,
                    reqSize);
            option.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory
                    .decodeFileDescriptor(fd, null, option);

            return bitmap;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
//            IOUtils.closeQuietly(is);
        }

        return null;
    }

    private static int caclulateInSampleSize(BitmapFactory.Options option,
                                             int rw, int rh) {
        int inSampleSize = 1;
        int ow = option.outWidth;
        int oh = option.outHeight;

        if (ow > rw || oh > rh) {
            final int hr = Math.round((float) oh / (float) rh);
            final int wr = Math.round((float) ow / (float) rw);
            inSampleSize = hr < wr ? hr : wr;
        }

        return inSampleSize;
    }

    public static void writeImage(File file, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }

    public static Bitmap blurBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth() / 2;
        int h = bitmap.getHeight() / 2;

        Bitmap work = Bitmap.createScaledBitmap(bitmap, w, h, true);

        int[] pix = new int[w * h];

        work.getPixels(pix, 0, w, 0, 0, w, h);

        int radius = 4;
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.max(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = 0xff000000 | (dv[rsum] << 16) | (dv[gsum] << 8)
                        | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        work.setPixels(pix, 0, w, 0, 0, w, h);

        return work;
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static Bitmap decodeBitmap(Context context, int resImageId, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置越大，图片越不清晰，占用内存越小
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(resImageId), null, options);

        return bitmap;
    }

    /**
     * 对于闪屏页，要求的宽和高为 屏幕高和宽(这里要查看闪屏图片在不同分辨率手机占用的内存是否不一样)
     * @param context
     * @param resImageId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Bitmap decodeBitmap(Context context, int resImageId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resImageId, options);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        // 设置越大，图片越不清晰，占用内存越小
        options.inSampleSize = getInSampleSize(reqWidth, reqHeight, imageWidth, imageHeight);
        options.inJustDecodeBounds = false;

        Log.e("bitmapUtil",  options.inSampleSize + " // " + imageWidth + ", " + imageHeight + " // " + reqWidth + ", " + reqHeight);

        Bitmap bitmap = BitmapFactory.decodeStream(context.getResources().openRawResource(resImageId), null, null);

        // 压缩分辨率的情况：
        // 4.4   huawei p6   inSampleSize = 4 1080x1920  req: 720x1280   1.5   518400 byte
        // 5.1.1 vivo x6plus inSampleSize = 4 1080x1920  req: 1080x1920  1     1166400 byte  (增大2.25 = 1.5 * 1.5)

        // 没有压缩的情况：
        // 4.4   huawei p6   1080x1920  req: 720x1280   1.5   8294400 byte
        // 5.1.1 vivo x6plus 1080x1920  req: 1080x1920  1     8294400 byte  (增大2.25 = 1.5 * 1.5)

        Log.e("bitmap", bitmap.getByteCount() + ", " + bitmap.getAllocationByteCount());

        return bitmap;
    }

    public static int getInSampleSize(int reqWidth, int reqHeight, int actualWidth, int actualHeight) {
        double wRatio = (double) reqWidth / actualWidth;
        double hRatio = (double) reqHeight / actualHeight;
        double ratio = Math.min(wRatio, hRatio);
        float n = 1.0f;
        while ((n * ratio) <= 2) {
            n *= 2;
        }
        return (int)n;
    }
}
