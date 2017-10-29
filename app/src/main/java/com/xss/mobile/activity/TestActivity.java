package com.xss.mobile.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xss.mobile.R;
import com.xss.mobile.handler.CrashHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xss on 2016/11/5.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        init();
    }

    private void init() {
        CrashHandler.getInstance().init(TestActivity.this);

    }

    private void testAsyncTask() {
        AsyncTask<Map, Integer, String> task = new AsyncTask<Map, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Map... params) {
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected void onCancelled(String result) {
                super.onCancelled(result);
            }
        };

        HashMap paramsMap = new HashMap();
        task.execute(paramsMap);
    }
}
