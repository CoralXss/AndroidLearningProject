package com.xss.mobile.activity.photo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xss.mobile.R;
import com.xss.mobile.utils.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xss on 2016/10/25.
 * desc: 相册：Bitmap截小图，Uri截大图   拍照，因为图片太大，所以都以Uri截图
 * 使用 Bitmap返回数据，使用 Uri不返回数据
 */
public class GetPictureActivity extends Activity {
    public static final int REQUEST_CAMERA = 0x0001;
    public static final int REQUEST_GALLERY = 0x0002;
    String TAG = "GetPictureActivity";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_picture);

        imageView = (ImageView) findViewById(R.id.imageView);

        findViewById(R.id.btn_from_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        findViewById(R.id.btn_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        test();
    }

    /**
     * 查看应用最高可用内存
     */
    private void test() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  // max memory = 196608 200M
        Log.e(TAG, "max memory = " + maxMemory);
    }

    private void openCamera() {
        PackageManager pm = getPackageManager();
        // 1.先检查设备上是否有照相机,没有就禁用相机相关功能
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 2.检查有无应用来处理该intent，如果没有就会Crash
            if (intent.resolveActivity(pm) != null) {
                takeCamera2(intent);
            }
        }
    }

    /**
     * 拍摄后不保存到手机中
     * @param intent
     */
    private void takeCamera1(Intent intent) {
        startActivityForResult(intent, REQUEST_CAMERA);
    }



    /**
     * 拍摄后保存图片到手机
     * @param intent
     */
    private void takeCamera2(Intent intent) {
        File imageFile = null;
        try {
            imageFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageFile != null) {
            // 保存到手机
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    /**
     * 保存拍摄的照片到公共外部存储中
     * @return
     */
    private String mCurrentPhotoPath = "";
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.e(TAG, "camera photo path = " + mCurrentPhotoPath);
        return imageFile;
    }

    private void openGallery() {

    }

    /**
     * 内存有限时，管理全尺寸图片很耗内存，通过缩放图片到目标视图尺寸，再加载到内存，可显著降低内存使用
     * BitmapFactory解码文件用decodeFile，解码资源文件用 decodeResource，解码网络图片用decodeStream
     */
    private void getCompressedPicture() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
        Log.e(TAG, "imageView size: " + "w = " + targetW + ", " + "h = " + targetH);  // 400 * 400   200 * 2 density

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        int imageW = options.outWidth;
        int imageH = options.outHeight;
        Log.e(TAG, "picture size: " + "w = " + imageW + ", " + "h = " + imageH); // w = 2448, h = 3264 图片原始尺寸

        int scale = Math.min(imageW / targetW, imageH / targetH);   // 压缩6倍
        options.inJustDecodeBounds = false;
//        options.inSampleSize = scale;
        options.inPurgeable = true;

        final Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        Log.e(TAG, bitmap.getByteCount() + "");
//        imageView.setImageBitmap(bitmap);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String base64 = ImageUtil.bitmapToBase64(bitmap);
                Log.e(TAG + "base", base64);

                Log.e(TAG, "Convert to bitmap");
                Bitmap btm = ImageUtil.base64ToBitmap(base64);
                imageView.setImageBitmap(btm);
            }
        });
    }

    private void showPictureDirect(Intent data) {
        Bundle bundle = data.getExtras();
        // 从 data中取出缩略图适用于作为图标
        Bitmap bitmap = (Bitmap) bundle.get("data");
        imageView.setImageBitmap(bitmap);
    }

    private void showCompressPicture() {
        getCompressedPicture();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    showCompressPicture();
                    break;
                case REQUEST_GALLERY:

                    break;
            }
        }
    }



}
