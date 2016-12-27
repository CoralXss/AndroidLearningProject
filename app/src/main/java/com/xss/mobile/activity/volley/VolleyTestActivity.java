package com.xss.mobile.activity.volley;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fangdd.android.volley.AuthFailureError;
import com.fangdd.android.volley.NetworkResponse;
import com.fangdd.android.volley.Request;
import com.fangdd.android.volley.RequestQueue;
import com.fangdd.android.volley.Response;
import com.fangdd.android.volley.VolleyError;
import com.fangdd.android.volley.toolbox.JsonObjectRequest;
import com.fangdd.android.volley.toolbox.JsonRequest;
import com.fangdd.android.volley.toolbox.StringRequest;
import com.fangdd.android.volley.toolbox.Volley;
import com.xss.mobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xss on 2016/10/14.
 * desc:
 * 1、四种不同的请求Request:
 *    1)StringRequest 请求一条普通的数据；
 *    2）JsonRequest请求Json数据；
 *    3）ImageRequest请求网络上的图片。
 * 2、网络上的两种传输格式：JSON和XML：
 *    如果要传输xml格式的数据，可以自己定制Request。
 */
public class VolleyTestActivity extends Activity {
    private static final String TAG = VolleyTestActivity.class.getSimpleName();

    private TextView tv_content;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_test);

        requestQueue = Volley.newRequestQueue(this);

        tv_content = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testStringRequest();
//
//                testJsonRequest();
            }
        });
    }

    private void testStringRequest() {
        String url = "http://www.baidu.com";
        // 默认get请求
        StringRequest request = getRequest(url, Request.Method.GET);
        request.setShouldCache(true);
        requestQueue.add(request);
    }

    private StringRequest getRequest(String url, int method) {
        StringRequest request = null;
        switch (method) {
            case Request.Method.GET:
                request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        tv_content.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage(), error);
                    }
                });
                break;
            case Request.Method.POST:
                request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", "xss");

                        return map;
                    }
                };
                break;
        }
        return request;
    }

    private void testJsonRequest() {
        String url = "http://m.weather.com.cn/data/101010100.html";
        Request request = getJsonRequest(url, Request.Method.GET);
        requestQueue.add(request);
    }

    /**
     * JsonRequest两个子类：一个请求json数据JsonObjectRequest，一个请求Json数组JsonArrayRequest
     * @param url
     * @param method
     */
    private Request getJsonRequest(String url, int method) {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "json: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                Log.d(TAG, response.toString() );

                return null;
            }

        };
        return request;
    }
}
