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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import java.security.Permission;
import java.util.jar.Manifest;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        // 加载 asset目录下的本地html文件：
        mUrl = "file:///android_asset/web_app_upload_image.html";
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
    private static final int REQUEST_CROP_PHOTO = 0x11;
    private ValueCallback<Uri> mSingleFileCallback;    // 图片选择4.4以下
    private ValueCallback<Uri[]> mMultiFileCallback;   // 图片选择5.0以上
    private Uri imageUri;
    private void setWebChromeClient() {
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            tv_title.setText(title);
        }

        // For 5.0以上
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            Log.e("Webview", "onShowFileChooser + 5.0");
            mMultiFileCallback = filePathCallback;
            openGallery();

            return true;  // 一定要return true 防止下次回到 WebView页面重新调用，抛异常 duplicate result
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.e("Webview", "openFileChooser + 3.0");
            mSingleFileCallback = uploadMsg;
            openGallery();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Log.e("Webview", "openFileChooser + 3.0+");
            mSingleFileCallback = uploadMsg;
            openGallery();
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.e("Webview", "openFileChooser + 4.1");
            mSingleFileCallback = uploadMsg;
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_LOAD_IMAGE_FROM_GALLERY);
    }

    /**
     * 从相册截取小图
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectY", 1);
        intent.putExtra("aspectX", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);  // false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    private void upload(Intent data) {
        if (mSingleFileCallback != null) {  // 5.0以下处理方式

            mSingleFileCallback.onReceiveValue(imageUri);
            mSingleFileCallback = null;

        } else if (mMultiFileCallback != null) {  // 5.0版本以上
            Uri[] uris = null;
            if (data == null) {
                uris = new Uri[] {imageUri};
            } else {

                String dataString = data.getDataString();
                    /*
                    // 多选图片[图片一定要压缩，转化为base64网页加载很慢]
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        int size = clipData.getItemCount();
                        uris = new Uri[size];
                        for (int i = 0; i < size; i++) {
                            // 将所选图片的 url保存到 uris数组中
                            uris[i] = clipData.getItemAt(i).getUri();
                        }
                    } */
                if (!TextUtils.isEmpty(dataString)) {
                    uris = new Uri[] {Uri.parse(dataString)};
                }
            }

            if (uris == null) {
                mMultiFileCallback.onReceiveValue(null);
            } else {
                mMultiFileCallback.onReceiveValue(uris);
            }
            mMultiFileCallback = null;
        }
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
                Log.e("image", "before zoom " + (temp.length() ) + "B");
                tempUri = Uri.fromFile(temp);
                imageUri = tempUri;

                upload(data);
            }

        } else if (requestCode == REQUEST_CROP_PHOTO) {


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
