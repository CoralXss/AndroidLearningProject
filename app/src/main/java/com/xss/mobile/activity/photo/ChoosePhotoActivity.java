package com.xss.mobile.activity.photo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xss.mobile.BuildConfig;
import com.xss.mobile.R;
import com.xss.mobile.utils.CommonUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChoosePhotoActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_CODE_GALLERY = 0x11;
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x12;
    private static final int REQUEST_CODE_CROP = 0x13;

    private Button btn_take_photo, btn_from_gallery;
    private RadioGroup radioGroup;
    private RadioButton rb_large, rb_small;

    private ImageView iv_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);

        initView();
    }

    private void initView() {
        btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
        btn_from_gallery = (Button) findViewById(R.id.btn_from_gallery);
        rb_large = (RadioButton) findViewById(R.id.rb_large);
        rb_small = (RadioButton) findViewById(R.id.rb_small);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        iv_photo = (ImageView) findViewById(R.id.iv_photo);

        btn_take_photo.setOnClickListener(this);
        btn_from_gallery.setOnClickListener(this);
    }

    private String TAG = ChoosePhotoActivity.class.getSimpleName();

    private void takePhoto() {

        imageUri = Uri.fromFile(imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", imageFile);
            Log.e(TAG,imageUri.getPath());
        } else {
            imageUri = Uri.fromFile(imageFile);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //图片存储的地方.
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    private void captureGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private static final String DEFAULT_IMAGE_FILE_NAME = "large.jpg";
    File imageFile = new File(Environment.getExternalStorageDirectory(), DEFAULT_IMAGE_FILE_NAME);
    Uri imageUri = Uri.fromFile(imageFile);

    /**
     * 裁剪图片
     * @param uri
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     * @param isReturnData
     */
    private void startZoomPhoto(Uri uri, int aspectX, int aspectY, int outputX, int outputY, boolean isReturnData) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);

        intent.putExtra("aspectX", aspectX);  // 1
        intent.putExtra("aspectY", aspectY);  // 1
        intent.putExtra("outputX", outputX);  // 300
        intent.putExtra("outputY", outputY);  // 300
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeed", true);

        intent.putExtra("return-data", isReturnData);  // 是否返回bitmap数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    /**
     * 将bitmap转化为相应的图片文件,最终得到的事被压缩的图片
     * @param bitmap
     * @param path
     */
    private void saveCompressdImage(Bitmap bitmap, String path) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path, false));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件转换为 ContentUri 注：不是所有File uri都可以转换为 contentUri，应该是多媒体相关才行
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO:
                startPhotoZoom(imageFile);

                break;

            case REQUEST_CODE_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();  // 值为contentUri
                    imageUri = uri;
                }

                String path = imageUri.getPath();
                File f = new File(CommonUtil.getPath(this, imageUri));
                imageUri = Uri.fromFile(f);

                startZoomPhoto(imageUri, 1, 1, 300, 300, false);

                break;

            case REQUEST_CODE_CROP:

                if (data != null) {
                    // 方式一： 小图直接使用 bitmap 存储，前提return-data为true
                    Bundle b = data.getExtras();
                    if (b != null) {
                        Bitmap bitmap = b.getParcelable("data");
                        // 若裁剪时 return-data为false，就从data的 Bundle中无法取出 Bitmap 数据；返回true，就可以获取Bitmap数据
                        if (bitmap != null) {
                            iv_photo.setImageBitmap(bitmap);
                            return;
                        }
                    }

                    // 方式二：decodeUriAsBitmap 大图采用uri 解析为bitmap
                    Uri outputUri = data.getData();
                    try {
                        Bitmap btm = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
                        iv_photo.setImageBitmap(btm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // 方式三：直接使用uri显示图片
//                    iv_photo.setImageURI(imageUri);
                }

                break;

            default:
                break;
        }
    }

    public void startPhotoZoom(File file) {
        Uri sourceUri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sourceUri = getImageContentUri(this, file);
        } else {
            sourceUri = Uri.fromFile(file);
        }
        intent.setDataAndType(sourceUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectY", 1);
        intent.putExtra("aspectX", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        // 为Content uri设置临时权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                takePhoto();

                break;
            case R.id.btn_from_gallery:
                captureGallery();

                break;
        }
    }
}
