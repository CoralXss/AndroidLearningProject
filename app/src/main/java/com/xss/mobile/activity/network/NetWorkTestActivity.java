package com.xss.mobile.activity.network;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xss.mobile.R;
import com.xss.mobile.utils.ViewUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xss on 2016/11/5.
 */
public class NetWorkTestActivity  extends Activity {
    private static final String TAG = NetWorkTestActivity.class.getSimpleName();

    private Button btn_download;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_network_test);

        btn_download = (Button) findViewById(R.id.btn_download);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetworkState()) {
                    download();
                }
            }
        });

        btn_download.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
    }

    /**
     * 检测网络是否可用
     */
    public boolean checkNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(NetWorkTestActivity.this, "Network is connected !", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Log.d(TAG, "No network");
            return false;
        }
    }

    private void download() {
        new DowanloadWebPageText().execute("http://www.baidu.com");
    }

    private class DowanloadWebPageText extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            tv_result.setText(s);
        }
    }

    private String downloadUrl(String myUrl) throws IOException {
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();

            if (response == 200) {
                is = conn.getInputStream();

                String content = readIt(is, len);
                return content;
            }
            return "Get nothing";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return "Get nothing";
    }

    public String readIt(InputStream is, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(is, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private void downloadApk() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int heapSize = am.getMemoryClass();  // 单位MB
        Log.e(TAG, "heap size = " + heapSize + " fileSize = " + getFilesDir().getAbsolutePath()
                + ", cacheSize = " + getCacheDir().getAbsolutePath());

        int screenWidth = ViewUtil.getScreenWidth(this);
        int screenHeight = ViewUtil.getScreenHeight(this);

        String str = Build.BOARD + ", " + Build.BOOTLOADER + ", " + Build.BRAND + ", " + Build.DEVICE + ", \n" +
                Build.DISPLAY + ", " + Build.HARDWARE + ", " + Build.HOST + ", " + Build.MANUFACTURER + ", \n" +
                Build.MODEL + ", " + Build.PRODUCT + ", " + Build.SERIAL + ", " + Build.TYPE;
        tv_result.setText(str);

        String apkUrl = "https://a.apk";
        Uri apkUri = Uri.parse(apkUrl);
        DownloadManager.Request request = new DownloadManager.Request(apkUri);
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "a-v8.3.0.apk");

//        ContentValues values = request.toContentValues("");
//        Uri downloadUri = mResolver.insert(Downloads.Impl.CONTENT_URI, values);
        long id = Long.parseLong(apkUri.getLastPathSegment());


        Log.e(TAG, str + ", \n" + "download id = " + id);
    }

    private void testUri() {
        String apkName = "/download/agent-v" + ".apk";

        final String fileUrl = Environment.getExternalStorageDirectory() + apkName;
        Uri uri = Uri.fromFile(new File(fileUrl));

        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Uri destUri = Uri.withAppendedPath(Uri.fromFile(downloadDir), apkName);

        Log.e(TAG, uri.toString() + "\n" + destUri.toString());
    }
}
