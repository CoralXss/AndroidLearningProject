package com.xss.mobile.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class AppUtils {
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setIsShowView(ImageView iv, boolean isShow) {
        if (iv == null) {
            return;
        }
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                iv.setAlpha(1.0f);
            else
                iv.setAlpha(255);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                iv.setAlpha(0.0f);
            else
                iv.setAlpha(0);
        }

    }

    @SuppressLint("NewApi")
    public static void setIsShowView(View iv, boolean isShow) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                iv.setAlpha(1.0f);
            else
                iv.setAlpha(255);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                iv.setAlpha(0.0f);
            else
                iv.setAlpha(0);

        }

    }

    public static String convertFloatToString(float f) {
        if (f - (int) f == 0.0) {
            return Float.toString(f).split("\\.")[0];
        } else {
            return Float.toString(f);
        }
    }

    public static String convertDoubleToString(double f) {
        if (f - (int) f == 0.0) {
            return Double.toString(f).split("\\.")[0];
        } else {
            return Double.toString(f);
        }
    }

    @SuppressLint("TrulyRandom")
    public static String desString(String data) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec("f@#2&ds=".getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);

            String jumpKey = Base64.encodeToString(cipher.doFinal(data.getBytes()), 0);
            if (!TextUtils.isEmpty(jumpKey)) {
                jumpKey = jumpKey.replace("\n", "");
            }
            return jumpKey;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getUriQuery(String uri) {
        JSONObject json = new JSONObject();
        String[] params = uri.replaceAll("\\s*", "").split("&");
        for (int i = 0, l = params.length; i < l; i++) {
            try {
                String[] map = params[i].trim().split("=");
                if (map.length == 2 && map[0] != null
                        && map[0].trim().length() > 0 && map[1] != null
                        && map[1].trim().length() > 0) {
                    try {
                        json.put(URLDecoder.decode(map[0], "UTF-8"),
                                URLDecoder.decode(map[1], "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                }
            } catch (Exception e) {
                e.toString();
            }
        }
        return json;
    }

    public static String objectToBase64(Object object) {
        String str = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            str = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            oos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return str;
    }

    public static Object base64ToObject(String encode) {
        Object obj = null;
        try {
            byte[] data = Base64.decode(encode, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
            ois.close();
            bais.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return obj;
    }

    public static void deleteFiles(Set<String> files, File parent) {
        if (files != null && !files.isEmpty()) {
            Iterator<String> it = files.iterator();
            while (it.hasNext()) {
                File file = null;
                try {
                    if (parent != null) {
                        file = new File(parent, it.next());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public static Set<String> getFilesInDirStartMatch(File dir, String match) {
        Set<String> data = null;
        if (dir.exists() && dir.isDirectory()) {
            data = new HashSet<String>();
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith(match)) {
                    data.add(files[i].getName());
                }
            }
        }
        return data;
    }

    public static void setTextWithDefaultValue(TextView textview, CharSequence value, CharSequence defaultValue) {
        if (textview != null) {
            if (!TextUtils.isEmpty(value)) {
                textview.setText(value);
            } else {
                textview.setText(defaultValue);
            }
        }
    }

    public static Bitmap getDefaultBitmap(Bitmap target, Resources res, @DrawableRes int id) {
        if (target instanceof Bitmap) {
            return target;
        } else {
            return BitmapFactory.decodeResource(res, id);
        }
    }

    public static String cleanBlankToString(String des) {
        char[] chars = des.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (Character c : chars) {
            if (java.util.regex.Pattern.matches("\\d", String.valueOf(c))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
