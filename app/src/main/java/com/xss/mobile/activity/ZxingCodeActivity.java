package com.xss.mobile.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xss.mobile.R;
import com.xss.mobile.utils.CommonUtil;
import com.xss.mobile.utils.DensityUtil;

import java.io.File;
import java.util.Hashtable;

public class ZxingCodeActivity extends Activity {
    private int mQrSize;

    private LinearLayout ll_content;
    private ImageView iv_qrcode;

    private Bitmap posterBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_code);

        mQrSize = DensityUtil.dip2px(this, 80);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);

        iv_qrcode.setImageBitmap(createQR("http://www.baidu.com"));

        ll_content.post(new Runnable() {
            @Override
            public void run() {
                posterBitmap = Bitmap.createBitmap(ll_content.getWidth(),
                        ll_content.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(posterBitmap);
                ll_content.draw(canvas);
            }
        });

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posterBitmap == null || posterBitmap.isRecycled()) {
                    return;
                }

                final String path = "xss_" + System.currentTimeMillis() + "_poster";
                String posterPath = CommonUtil.savePosterImage(ZxingCodeActivity.this, path, posterBitmap);

                shareWeChat(posterPath);
            }
        });
    }

    private void shareWeChat(String path){
        Uri uriToImage = Uri.fromFile(new File(path));
        Intent shareIntent = new Intent();
        //发送图片到朋友圈
        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        //发送图片给好友。
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    protected Bitmap createQR(String str) {
        Bitmap bitmap = null;
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码方式
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//纠错等级
            hints.put(EncodeHintType.MARGIN, 0);//外围边距
            BitMatrix bitMatrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, mQrSize, mQrSize, hints);
            int[] pixels = new int[mQrSize * mQrSize];
            for (int y = 0; y < mQrSize; y++) {
                for (int x = 0; x < mQrSize; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * mQrSize + x] = 0xff000000;
                    } else {
                        pixels[y * mQrSize + x] = 0;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(mQrSize, mQrSize,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, mQrSize, 0, 0, mQrSize, mQrSize);

        } catch (WriterException e) {

        }
        return bitmap;
    }
}
