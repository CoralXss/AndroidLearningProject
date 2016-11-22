package com.xss.mobile.activity.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xss.mobile.R;

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
}
