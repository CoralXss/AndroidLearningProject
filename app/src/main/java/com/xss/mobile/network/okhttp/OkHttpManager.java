package com.xss.mobile.network.okhttp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by xss on 2016/11/29.
 */
public class OkHttpManager {
    private static final String TAG = OkHttpManager.class.getSimpleName();

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/png");


    private static OkHttpManager mInstance;

    private OkHttpClient okHttpClient;

    private OkHttpManager() {
        okHttpClient = new OkHttpClient.Builder().build();
    }

    public static OkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置自签名证书
     * Example：使用12306证书 /assets/srca.cer，在Application中设置
     * @param certificates
     */
    public void setCertificates(InputStream...certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream is: certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(is));

                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ee) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                       + Arrays.toString(trustManagers));
            }
            X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
            sslContext.init(null, new TrustManager[] {x509TrustManager}, null);

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = this.okHttpClient.newBuilder();
            builder.sslSocketFactory(sslSocketFactory, x509TrustManager);

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


    public void testGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1、获取封装好的OkHttp客户端-OkHttpClient
                try {
                    // 2、http请求体-Request
                    Request request = new Request.Builder()
                            .url("https://kyfw.12306.cn/otn/")
                            .method("POST", new RequestBody() {
                                @Override
                                public MediaType contentType() {
                                    return null;
                                }

                                @Override
                                public void writeTo(BufferedSink sink) throws IOException {

                                }
                            })
                            .header("Range", "bytes=0-")
                            .headers(new Headers.Builder().build())   // Headers.class
                            .addHeader("key", "value")
                            .get()
                            .post(null)   // RequestBody.class
                            .put(null)
                            .delete(null)
                            .patch(null)
                            .cacheControl(new CacheControl.Builder().build())   // CacheControl.class
                            .build();
                    // 没有添加 Callback，同步请求，在主线程中做耗时操作：NetworkOnMainThreadException
                    // 3、http响应体-Response
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String content = response.body().string();
                        Log.d(TAG, content);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 文件上传(只上传单个文件)
     */
    public void uploadFile(String url, String filePath) {
        File file = new File(Environment.getExternalStorageDirectory(), filePath);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(filePath.indexOf(".jpg") != -1 ? MEDIA_TYPE_IMAGE : MEDIA_TYPE_MARKDOWN, file))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "---upload failed---");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "---upload succeed---\n" + response.body().string());
            }
        });
    }

    /**
     * 上传文件：包含除了文件外的其他参数
     * @param url
     * @param filePath
     */
    public void uploadMultipartFile(String url, String filePath) {
        File file = new File(Environment.getExternalStorageDirectory(), filePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "I believe")
                .addFormDataPart("author", "Hans")
                .addFormDataPart("name", filePath.indexOf(".jpg") != -1 ? "Little_Ali.jpg" : "Little_Ali.txt",
                        RequestBody.create(filePath.indexOf(".jpg") != -1 ? MEDIA_TYPE_IMAGE : MEDIA_TYPE_MARKDOWN, file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Log.e(TAG, "---upload start---");
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "---upload failed---");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "---upload succeed---\n" + response.body().string());
            }
        });
    }

    /**
     * 文件下载
     * @param url
     */
    public void downloadFile(String url, final String savePath) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "---下载文件failed---");
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    // 注：OkHttp在读写IO流之前，不能打印任何同 Response有关的日志，否则会一直抛IO异常: closed
                    // Reading or printing the outStream will close it
                    // string()方法内部调用 bytes()方法，调用过一次后，就close掉了is，内部成员 close = true，再次读取就一直是 close的状态
//                    Log.e(TAG, response.body().string());

//                    Reader reader = response.body().charStream();
//
//                    byte[] byts = response.body().bytes();

                    InputStream is = response.body().byteStream();
                    File file = new File(savePath);
                    FileOutputStream fos = new FileOutputStream(file);
                    int len = 0;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }

//                    fos.write(byts);

                    fos.flush();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "---下载文件successed---");
            }
        });
    }
}
