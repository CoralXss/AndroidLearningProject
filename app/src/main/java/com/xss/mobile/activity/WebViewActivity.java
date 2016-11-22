package com.xss.mobile.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xss.mobile.R;

/**
 * Created by xss on 2016/10/18.
 */
public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = (WebView) findViewById(R.id.webView);
        // ToDo 这里的 url要做特殊处理，要替换特殊字符，不然转成uri会被截断
        String mUrl = "http://mp.weixin.qq.com/s?__biz=MzI5MDMyNTE2OQ==&mid=2247483792&idx=1&sn=d51793ad780d6ac55403c5867b2d05d3#rx&share_id=133";   // &jumpkey=xQLrsaDvR650Yu9mZ6/NjDNpqOpjBgEY&cityname=%E5%8D%97%E4%BA%AC";
        mUrl = mUrl.replace("#", "%23");

        Uri uri = Uri.parse(mUrl);
        String shareId = uri.getQueryParameter("share_id");

        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        if (!TextUtils.isEmpty(shareId)) {
            Log.e("shareId", shareId);
        }
        webView.loadUrl(mUrl);
//        //设置Web视图
        webView.setWebViewClient(new WebViewClient());
    }
}
