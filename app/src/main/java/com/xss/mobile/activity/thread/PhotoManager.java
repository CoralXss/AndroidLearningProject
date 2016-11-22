package com.xss.mobile.activity.thread;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by xss on 2016/11/15.
 */
public class PhotoManager {
    private static PhotoManager mInstance;

    private static final int DOWNLOAD_COMPLETE = 0x00;

    // 获取处于活动状态的内核数量，可能少于设备的实际内核总数
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // 线程池管理队列
    private BlockingDeque<Runnable> mDecodeWorkQueue = new LinkedBlockingDeque<>();

    private static final int KEEP_ALIVE_TIME = 1;

    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor mDecodeThreadPool = new ThreadPoolExecutor(
            NUMBER_OF_CORES,    // 初始线程池大小
            NUMBER_OF_CORES,    // 最大线程池大小
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_UNIT,
            mDecodeWorkQueue
    );

    private PhotoManager() {}

    static {
        mInstance = new PhotoManager();
    }

    public void handleState(int state) {
        switch (state) {
            case DOWNLOAD_COMPLETE:
                mDecodeThreadPool.execute(new PhotoDecodeRunnable());
                break;
        }
    }

    /**
     *
     */
    public void cancelAll() {
        Runnable[] runnableArray = new Runnable[mDecodeWorkQueue.size()];

        mDecodeWorkQueue.toArray(runnableArray);

        int len = runnableArray.length;

        synchronized (mInstance) {
            for (int i = 0; i < len; i++) {
//                Thread thread = runnableArray[i].mThread;
            }
        }
    }

    public static void useCA() {
        InputStream is = null;
        Certificate ca = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            is = new BufferedInputStream(new FileInputStream("load-der.crt"));
            ca = cf.generateCertificate(is);
            Log.e("PhotoManager", String.valueOf(((X509Certificate)ca).getSubjectDN()));

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            URL url = new URL("https://certs.cac.washington.edu/CAtest");
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());

            InputStream iis = urlConnection.getInputStream();


        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
