package com.xss.mobile.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.xss.mobile.R;
import com.xss.mobile.activity.launchmode.FirstActivity;
import com.xss.mobile.utils.AppUtils;
import com.xss.mobile.utils.CommonUtil;

import java.io.File;

/**
 * Created by xss on 2016/10/18.
 */
public class WebViewActivity extends Activity {
    private TextView tv_title, tv_click;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_click = (TextView) findViewById(R.id.tv_click);

        webView = (WebView) findViewById(R.id.webView);
        // ToDo 这里的 url要做特殊处理，要替换特殊字符，不然转成uri会被截断
//        String mUrl = "http://mp.weixin.qq.com/s?__biz=MzI5MDMyNTE2OQ==&mid=2247483792&idx=1&sn=d51793ad780d6ac55403c5867b2d05d3#rx&share_id=133";   // &jumpkey=xQLrsaDvR650Yu9mZ6/NjDNpqOpjBgEY&cityname=%E5%8D%97%E4%BA%AC";
//        mUrl = mUrl.replace("#", "%23");
//        Uri uri = Uri.parse(mUrl);
//        String shareId = uri.getQueryParameter("share_id");
//        if (!TextUtils.isEmpty(shareId)) {
//            Log.e("shareId", shareId);
//        }

        String mUrl = "http://10.0.41.19:8099/TomcatTest/web_app_upload_image.html";
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        // 加载 asset目录下的本地html文件： mUrl = "file:///android_asset/web_app.html"
        webView.loadUrl(mUrl);
        //设置WebViewClient用来辅助WebView处理各种通知请求事件等，如更新历史记录、网页开始加载/完毕、报告错误信息等
        setWebViewClient();

        // 用于辅助WebView处理JavaScript的对话框、网站图标、网站标题以及网页加载进度等
        setWebChromeClient();

        // 使 H5可调用Native方法： android.nativeMethod()
        webView.addJavascriptInterface(new MyJsInterface(), "android");

        // 点击原生按钮，向H5页面发送数据，可更新H5页面
        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "from Native";
                // loadUrl必须在主线程中执行
                webView.loadUrl("javascript:toNative('" + msg + "')");
            }
        });
    }

    private void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {

            // 以下方法避免 自动打开系统自带的浏览器，而是让新打开的网页在当前的WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private static final int REQUEST_LOAD_IMAGE_FROM_GALLERY = 0x10;
    private ValueCallback<Uri> mSingleFileCallback;
    private ValueCallback<Uri[]> mMultiFileCallback;
    private Uri imageUri;
    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tv_title.setText(title);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.e("Webview", "onShowFileChooser");
                mMultiFileCallback = filePathCallback;
                openGallery();

                return true;  // 一定要return true 防止下次回到 WebView页面重新调用，抛异常 duplicate result
            }

            // 5.0以下的文件上传监听方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.e("Webview", "openFileChooser");
                mSingleFileCallback = uploadMsg;
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_LOAD_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOAD_IMAGE_FROM_GALLERY) {
            if (mSingleFileCallback == null && mMultiFileCallback == null) {
                return;
            }
            Uri tempUri = null;
            if (data != null) {
                String url = CommonUtil.getPath(WebViewActivity.this, data.getData());
                File temp = new File(url);
                tempUri = Uri.fromFile(temp);
            }

            if (mSingleFileCallback != null) { // 5.0以下处理方式
                if (data != null) {
                    mSingleFileCallback.onReceiveValue(tempUri);
                } else {
                    mSingleFileCallback.onReceiveValue(imageUri);
                }
                mSingleFileCallback = null;
            } else if (mMultiFileCallback != null) { // 5.0版本以上
                Uri[] uris = null;
                if (data == null) {
                    uris = new Uri[] {imageUri};
                } else {
                    // 多选图片[图片一定要压缩，转化为base网页加载很慢]
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        int size = clipData.getItemCount();
                        uris = new Uri[size];
                        for (int i = 0; i < size; i++) {
                            // 将所选图片的 url保存到 uris数组中
                            uris[i] = clipData.getItemAt(i).getUri();
                        }
                    }
                    if (!TextUtils.isEmpty(dataString)) {
                        uris = new Uri[] {Uri.parse(dataString)};
                    }
                }

                if (uris == null) {
                    mMultiFileCallback.onReceiveValue(null);
                    mMultiFileCallback = null;
                } else {
                    mMultiFileCallback.onReceiveValue(uris);
                    mMultiFileCallback = null;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回键处理
        if (webView.canGoBack() && (keyCode == KeyEvent.KEYCODE_BACK)) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyJsInterface {

        @JavascriptInterface
        public void toastMessage(String msg) {
            Toast.makeText(WebViewActivity.this, "app to H5 toast " + msg, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WebViewActivity.this, FirstActivity.class);
            WebViewActivity.this.startActivity(intent);
        }

        @JavascriptInterface
        public void openActivity() {
            Intent intent = new Intent(WebViewActivity.this, FirstActivity.class);
            WebViewActivity.this.startActivity(intent);
        }
    }
}
