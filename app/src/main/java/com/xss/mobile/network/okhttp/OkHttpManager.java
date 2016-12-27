package com.xss.mobile.network.okhttp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xss on 2016/11/29.
 */
public class OkHttpManager {
    private static final String TAG = OkHttpManager.class.getSimpleName();

    private static OkHttpManager mInstance;

    private OkHttpClient okHttpClient;

    private OkHttpManager() {
        okHttpClient = new OkHttpClient();
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
                    Request request = new Request.Builder().url("https://kyfw.12306.cn/otn/").build();
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
}
