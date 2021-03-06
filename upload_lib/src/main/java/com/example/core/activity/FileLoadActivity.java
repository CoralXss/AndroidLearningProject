package com.example.core.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.core.HttpManager;
import com.example.core.entity.FileEntity;
import com.example.core.http.HttpMethod;
import com.example.core.http.LoadRequest;
import com.example.core.http.MediaType;
import com.example.core.R;
import com.example.core.http.MultipartBody;
import com.example.core.http.RequestBody;
import com.example.core.callback.LoadCallback;
import com.example.core.util.LoadSharedPrefUtil;

import java.io.File;


public class FileLoadActivity extends AppCompatActivity implements View.OnClickListener {

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/png");

    public static final String TAG = FileLoadActivity.class.getSimpleName();

    private EditText edt_ip;
    private String requestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_load);

        edt_ip = (EditText) findViewById(R.id.edt_ip);
        findViewById(R.id.btn_download).setOnClickListener(this);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_upload_with_params).setOnClickListener(this);

        findViewById(R.id.btn_crash).setOnClickListener(this);

    }

    /**
     * 断点续传，已经将请求返回的 Response中的 Etag 以文件名为 key，value=Etag存到 sp文件中，下次若请求同一文件，先判断缓存是否有 Etag：
     * 没有，即是第一次请求，有，就设置到 Request 的 header中， "If-Range" = Etag
     */

    private void upload() {
        requestUrl = "http://fsupload.fangdd.net/put_file.php";

        String fileName = "myDownload.txt";
        fileName = "act.jpg";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_IMAGE, file);

        LoadRequest request = new LoadRequest.Builder()
                .url(requestUrl)
                .setRequestBody(requestBody)
                .setMethod(HttpMethod.POST)
                .build();

        HttpManager.getInstance().post(request, new LoadCallback() {
            @Override
            public void onStart() {
                Log.e(TAG, "onStart");
            }

            @Override
            public void onProgress(long total, long current) {
                Log.e(TAG, "onProgress" + total + ", " + current);
            }

            @Override
            public void onSuccess(Object result) {
                Log.e(TAG, "upload success" + (result instanceof File));
                if (result != null) {
                    Log.e(TAG, "result = " + result);
                }
                Toast.makeText(FileLoadActivity.this, "upload success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code, String msg) {
                Log.e(TAG, "onFailed");
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish");
            }
        });

    }

    private void uploadWithParams() {
        requestUrl = projectAddr + "/UploadFileServlet"; // MyUploadServlet   UploadFileServlet;

        File file = new File(Environment.getExternalStorageDirectory(), "myDownload.txt");

        // 多文件上传，每创建一个 body，就将 RequestBody写到
        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("author", "xss_卡洛")
                .addFormDataPart("name", "myUpload0", RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .addFormDataPart("name", "myUpload1", RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        final LoadRequest request = new LoadRequest.Builder()
                .url(requestUrl)
                .setMethod(HttpMethod.POST)
                .setRequestBody(multipartBody)
                .build();

        HttpManager.getInstance().post(request, new LoadCallback() {
            @Override
            public void onStart() {
                Log.e(TAG, "onStart");
            }

            @Override
            public void onProgress(long total, long current) {
                Log.e(TAG, "onProgress" + total + ", " + current);
            }

            @Override
            public void onSuccess(Object result) {
                Log.e(TAG, "upload success" + (result instanceof File));
                if (result != null) {
                    Log.e(TAG, "result = " + result);
                }
                Toast.makeText(FileLoadActivity.this, "upload success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code, String msg) {
                Log.e(TAG, "onFailed");
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish");
            }
        });

    }

    private void download() {
        requestUrl = projectAddr + "/DownloadFileServlet";  // MyDownloadServlet
//        requestUrl = "https://fsxf.fangdd.com/xf/loJjNBcAOcAsrIW3Ly5c1UEpcV8s.apk";

        final String filePath = Environment.getExternalStorageDirectory() + "/myDownload.txt";
        String eTag = LoadSharedPrefUtil.getInstance(FileLoadActivity.this).getETag(filePath);
        final LoadRequest request = new LoadRequest.Builder()
                .url(requestUrl)
                .setMethod(HttpMethod.GET)
                .setSaveFilePath(filePath)
                .addHeaders("Etag", eTag)
                .setIsAutoResume(true)
                .build();

        HttpManager.getInstance().get(request, new LoadCallback<FileEntity>() {
            @Override
            public void onStart() {
                Log.e(TAG, "onStart");
                Toast.makeText(FileLoadActivity.this, "download start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(long total, long current) {
                Log.e(TAG, "onProgress" + total + ", " + current);
            }

            @Override
            public void onSuccess(FileEntity result) {
                Log.e(TAG, "download success" + (result instanceof FileEntity));
                Toast.makeText(FileLoadActivity.this, "download success", Toast.LENGTH_SHORT).show();

                LoadSharedPrefUtil.getInstance(FileLoadActivity.this).setETag(filePath, result.eTag);
            }

            @Override
            public void onFailed(int code, String msg) {
                Log.e(TAG, "onFailed");
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish");
            }
        });

    }

    private String projectAddr = "";
    @Override
    public void onClick(View v) {
        // 请求服务器上的 ifo.txt文件
        projectAddr = "http://" + edt_ip.getText().toString() + ":8099/download";

        if (v.getId() == R.id.btn_download) {
            download();

        } else if (v.getId() == R.id.btn_upload) {
            upload();

        } else if (v.getId() == R.id.btn_upload_with_params) {

            uploadWithParams();
        } else if (v.getId() == R.id.btn_crash) {
            int i = 1 / 0;
            Log.e(TAG, i + "");
        }
    }
}
