package com.xss.mobile.network.volley;

import android.content.Context;
import android.text.TextUtils;

import com.fangdd.android.volley.AuthFailureError;
import com.fangdd.android.volley.DefaultRetryPolicy;
import com.fangdd.android.volley.NetworkError;
import com.fangdd.android.volley.NoConnectionError;
import com.fangdd.android.volley.ParseError;
import com.fangdd.android.volley.Request;
import com.fangdd.android.volley.RequestQueue;
import com.fangdd.android.volley.Response;
import com.fangdd.android.volley.ServerError;
import com.fangdd.android.volley.TimeoutError;
import com.fangdd.android.volley.VolleyError;
import com.fangdd.android.volley.toolbox.StringRequest;
import com.fangdd.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by xss on 2016/11/18.
 */
public class RequestManager {
    private static final String TAG = RequestManager.class.getSimpleName();

    private static RequestQueue mRequestQueue;
    private static RequestManager mInstance;

    private RequestManager() {}

    public static RequestManager getInstance(Context context) {
        if (null == mInstance) {
            synchronized (RequestManager.class) {
                if (null == mInstance) {
                    mInstance = new RequestManager();
                    mRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 取消特定标签的网络请求
     * @param tag
     */
    public void cancelAll(Object tag) {
        if (null != tag) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private String getRequestUrlPrefix(String url) {
        // "http://10.0.41.31:8099/WebTest"
        StringBuffer sb = new StringBuffer(url);
//        sb.append(url);
        return sb.toString();
    }

    public void sendRequest(int method, String url, Object tag, final RequestParams params, boolean isShowToast, RequestListener listener) {
        // 请求之前先清楚之前的请求
        cancelAll(tag);

        if (listener != null) {
            listener.onPreExecute();
        }

        StringRequest request = null;
        switch (method) {
            case Request.Method.GET:
            case Request.Method.DELETE:
                // get方式，参数直接拼接在url之后
                url = getRequestUrlPrefix(url) + params.getBody();
                request = new StringRequest(method, url,
                        new SuccessResponseListener(listener, isShowToast),
                        new ErrorResponseListener(listener, isShowToast)) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return params.getHeader();
                    }
                };
                break;
            case Request.Method.POST:
            case Request.Method.PUT:
                request = new StringRequest(method, url,
                        new SuccessResponseListener(listener, isShowToast),
                        new ErrorResponseListener(listener, isShowToast)) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return params.getHeader();
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return params.getBody().getBytes();
                    }
                };
                break;
            default:
                break;
        }
        if (request != null) {
            // 给请求设置标签
            if (null != tag) {
                request.setTag(tag);
            }
            // 设置重连
            request.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(request);
        }
    }

    private void onSuccess(RequestListener listener, String response) {
        if (listener != null) {
            listener.onSuccess(response);
            listener.onFinish();
        }
    }

    private void onFail(RequestListener listener, String code, String msg, boolean isShowToast) {
        if (listener != null) {
            listener.onFail(code, msg);
            listener.onFinish();
        }
    }


    private class SuccessResponseListener implements Response.Listener<String> {
        private RequestListener mListener;
        private boolean isShowToast;

        public SuccessResponseListener(RequestListener mListener, boolean isShowToast) {
            this.mListener = mListener;
            this.isShowToast = isShowToast;
        }

        @Override
        public void onResponse(String response) {
            if (TextUtils.isEmpty(response)) {
                onFail(mListener, "-1", "", isShowToast);
                return;
            }

            JSONObject json = null;
            String code = "-1";
            String msg = "";
            try {
                json = new JSONObject(response);
                code = json.optString("code");
                msg = json.optString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
                onFail(mListener, code, msg, isShowToast);
            }

            if ("-1".equals(code) || json == null) {
                onFail(mListener, code, msg, isShowToast);
            } else {
                if ("200".equals(code)) {
                    onSuccess(mListener, json.optString("data"));
                } else {
                    onFail(mListener, code, msg, isShowToast);
                }
            }
        }
    }

    private class ErrorResponseListener implements Response.ErrorListener {
        private RequestListener mListener;
        private boolean isShowToast;

        public ErrorResponseListener(RequestListener mListener, boolean isShowToast) {
            this.mListener = mListener;
            this.isShowToast = isShowToast;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            String msg = error.getMessage();
            if (error instanceof ServerError) {
                msg = "系统走神了，请稍后重试";
            } else if (error instanceof TimeoutError) {
                msg = "请求超时，请稍后重试";
            } else if (error instanceof NetworkError) {
                msg = "网络异常，请稍后重试";
            } else if (error instanceof NoConnectionError) {
                msg = "网络状态不好，请稍后重试";
            } else if (error instanceof ParseError) {
                msg = "系统错误，请稍后重试";
            } else if (TextUtils.isEmpty(msg)) {
                msg = "网络异常，请稍后重试";
            }
            onFail(mListener, "-1", msg, isShowToast);
        }
    }

}
