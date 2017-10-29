package com.xss.mobile.activity.network;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xss.mobile.R;
import com.xss.mobile.network.okhttp.OkHttpManager;
import com.xss.mobile.utils.DensityUtil;
import com.xss.mobile.utils.FileUtils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AutoResumeDownloadActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AutoResumeDownloadActivity.class.getSimpleName();

    private static final String FILE_NAME = "/test.txt";

    private EditText edt_ip;
    private Button btn_download, btn_get, btn_post;
    private TextView tv_content;

    private FileUtils fileUtils;

    private String ip;
    private String downloadPath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0x01:
                    String s = fileUtils.readFileByLine(Environment.getExternalStorageDirectory() + FILE_NAME);
                    tv_content.setText(s);
                    Toast.makeText(AutoResumeDownloadActivity.this, msg.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 0x02:
                    tv_content.setText(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_resume_download);

        fileUtils = FileUtils.getInstance();

        initView();
    }

    private void initView() {
        edt_ip = (EditText) findViewById(R.id.edt_ip);
        btn_download = (Button) findViewById(R.id.btn_download);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_post = (Button) findViewById(R.id.btn_post);
        tv_content = (TextView) findViewById(R.id.tv_content);

        btn_download.setOnClickListener(this);
        findViewById(R.id.btn_download_okhttp).setOnClickListener(this);
        findViewById(R.id.btn_upload_okhttp).setOnClickListener(this);
        findViewById(R.id.btn_upload_multipart_okhttp).setOnClickListener(this);
        findViewById(R.id.btn_upload_multipart_connection).setOnClickListener(this);
        findViewById(R.id.btn_upload_xutil).setOnClickListener(this);
        btn_get.setOnClickListener(this);
        btn_post.setOnClickListener(this);
    }

    private void test() {
        Toast.makeText(this, "开始下载...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadByURLConnection();
            }
        }).start();
    }

    private void uploadByURLConnection(String downloadPath) {
        String filePath = "downInfo.txt";
        File uploadFile = new File(Environment.getExternalStorageDirectory(), filePath);

        Map<String, String> params = new HashMap<>();
        params.put("author", "xss");

        try {
            uploadMultipart(params, "name", uploadFile, "", downloadPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";
    /**
     * 带参文件上传
     */
    private void uploadMultipart(Map<String, String> params, String formName, File file, String saveFileName, String reqUrl) throws IOException {
        if (TextUtils.isEmpty(saveFileName)) {
            saveFileName = file.getName();
        }
        // 普通表单数据
        StringBuilder sb = new StringBuilder();
        for (String key: params.keySet()) {
            sb.append("--" + BOUNDARY + "\r\n")
              .append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n")
              .append("\r\n")
              .append(params.get(key) + "\r\n");
        }

        // 上传文件头
        sb.append("--" + BOUNDARY + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + formName + "\"; filename=\"" + saveFileName + "\"" + "\r\n");
        // 若服务器有文件类型的校验，必须加上 content-type
        sb.append("Content-Type: text/x-markdown" + "\r\n");
        sb.append("\r\n");

        // text/x-markdown;charset=utf-8

        byte[] headers = sb.toString().getBytes("UTF-8");
        byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
        Log.e(TAG, sb.toString());

        URL url = new URL(reqUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        connection.setRequestProperty("Content-Length",
                String.valueOf(headers.length + file.length() + endInfo.length));
        connection.setDoInput(true);

        OutputStream os = connection.getOutputStream();
        InputStream is = new FileInputStream(file);
        os.write(headers);

        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
        os.write(endInfo);

        is.close();
        os.close();

        if (connection.getResponseCode() == 200) {
            Log.e(TAG, "上传成功");
        }

    }

    /**
     * 下载文件
     * 方式一：使用 URLConnection 下载
     */
    private void downloadByURLConnection() {
        try {
            downloadPath = projectAddr + "/DownloadFileServlet";
            File targetFile = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

            // 发送 GET 请求
            String fileName = "?fileName=" + URLEncoder.encode("info.txt", "UTF-8");
            URL url = new URL(downloadPath + fileName);    Log.e(TAG, "url = " + downloadPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 默认为GET方法
            conn.setRequestMethod("GET");
            // 默认支持从服务端读取结果流，以下设置可省略
            conn.setDoInput(true);
            // 禁用网络缓存
            conn.setUseCaches(false);

            // 设置断点续传：请求头中方 "Range"
            if (targetFile.exists()) {
                conn.addRequestProperty("Range", "bytes=" + targetFile.length());
            } else {
                conn.addRequestProperty("Range", "bytes=0-");
            }

            // 配置完各参数后，通过调用 connect 建立 TCP连接，但并未真正获取数据
            // conn.connect() 可不必显式调用，当调用 conn.getInputStream()时内部会自动调用 connect()
            conn.connect();

            // 获取响应码
            int responseCode = conn.getResponseCode();
            // 获取响应 头部信息
            Map<String, List<String>> headers = conn.getHeaderFields();
            Log.e(TAG, "headers = " + headers);

            // 调用 getInputStream()方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
            InputStream is = conn.getInputStream();

            if (responseCode == 200 || responseCode == 206) {

                FileOutputStream fos = new FileOutputStream(targetFile, true);
                int len = -1;
                byte[] buf = new byte[10];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    break; // 便于测试，每次只读取一次
                }
                fos.close();
                is.close();

                Log.e(TAG, "---request end---");
                Message message = new Message();
                message.what = 0x01;
                message.obj = "下载成功";
                handler.sendMessage(message);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件(文本文件/图片)
     * 方式二：通过 OkHttp方式下载 (文件和图片均OK)
     */
    private void downloadByOkHttp(String downloadPath) {
        String path = Environment.getExternalStorageDirectory() + "/downInfo.txt";
//        path = Environment.getExternalStorageDirectory() + "/ali.jpg";
//        downloadPath = getHouseThumbImageUrl(path);
        OkHttpManager.getInstance().downloadFile(downloadPath, path);
    }

    public String getHouseThumbImageUrl(String url) {
        int w = DensityUtil.dip2px(this, 200);
        int h = DensityUtil.dip2px(this, 300);

        if (TextUtils.isEmpty(url))
            return url;
        String thumbStr = "thumb/" + w + "M" + h;
        if (!url.contains("orig")) {
            thumbStr += "/orig";
        }
        String thumbUrl = url.replace("image", thumbStr);
        return thumbUrl;
    }

    /**
     * 上传文件：上传包含文件和其他请求字段
     * @param downloadPath
     */
    private void uploadByOkHttp(String downloadPath) {
        String jpg = "/ali.jpg";
//        jpg = "downInfo.txt";
        OkHttpManager.getInstance().uploadFile(downloadPath, jpg);
    }

    /**
     * 上传文件：上传包含文件和其他请求字段
     * @param downloadPath
     */
    private void uploadMultipartByOkHttp(String downloadPath) {
        String jpg = "/ali.jpg";
        jpg = "myDownload.txt";
        OkHttpManager.getInstance().uploadMultipartFile(downloadPath, jpg);
    }

    private void getRequest() {

    }

    private void postRequest() {
        try {
            URL url = new URL(downloadPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            // 写入 post 请求实体
            OutputStream os = conn.getOutputStream();
            byte[] requestBody = new String("name=xss&age=24").getBytes("UTF-8");
            os.write(requestBody);

            conn.connect();

            // 记得调用输出流的 flush方法
            os.flush();
            os.close();

            int status = conn.getResponseCode();
            //
            InputStream is = conn.getInputStream();
            if (status == 200) {
                String result = fileUtils.getContentByInputStream(is);
                Log.e(TAG, "post result = " + result);

                Message message = new Message();
                message.what = 0x02;
                message.obj = "请求结果：" + result;
                handler.sendMessage(message);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String projectAddr;
    @Override
    public void onClick(View v) {
        ip = edt_ip.getText().toString();
        // 请求服务器上的 ifo.txt文件
        projectAddr = "http://" + ip + ":8099/download";

        switch (v.getId()) {
            case R.id.btn_download:

                test();
                break;

            case R.id.btn_download_okhttp:
//                downloadPath = projectAddr + "/ali.jpg";
                downloadPath = projectAddr + "/info.txt";
                downloadByOkHttp(downloadPath);
                break;

            case R.id.btn_upload_okhttp:
                downloadPath = projectAddr + "/UploadFileServlet";
                uploadByOkHttp(downloadPath);

                break;
            case R.id.btn_upload_multipart_okhttp:
                downloadPath = projectAddr + "/UploadFileServlet";
                uploadMultipartByOkHttp(downloadPath);

                break;
            case R.id.btn_upload_multipart_connection:
                downloadPath = projectAddr + "/UploadFileServlet";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadByURLConnection(downloadPath);
                    }
                }).start();

                break;
            case R.id.btn_upload_xutil:
                downloadPath = projectAddr + "/UploadFileServlet";
                download();
                break;
            case R.id.btn_get:
                getRequest();
                break;
            case R.id.btn_post:
                ip = edt_ip.getText().toString();
                // 请求服务器上的 ifo.txt文件
                downloadPath = "http://" + ip + ":8099/download/PostMethodTestServelt";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postRequest();
                    }
                }).start();

                break;
        }
    }



    private void download() {
        File uploadFile = new File(Environment.getExternalStorageDirectory(), "myDownload.txt");
        String patchPath = Environment.getExternalStorageDirectory() + "/post.txt";
        RequestParams params = new RequestParams(downloadPath);
        //设置断点续传
        params.setAutoResume(true);
        params.setConnectTimeout(200);
        params.setSaveFilePath(patchPath);

        // 设置多文件上传
        params.setMultipart(true);
        params.addBodyParameter("author", "Hans");
        // // 如果文件没有扩展名, 最好设置contentType参数
        params.addBodyParameter("name", uploadFile, "multipart/form-data", "myFileName");

        Log.e(TAG, "--onStart-- ");
        x.http().post(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.e(TAG, "--onSuccess-- " + result.getAbsolutePath());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
