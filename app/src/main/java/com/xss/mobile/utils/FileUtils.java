package com.xss.mobile.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by xss on 2017/3/10.
 */

public class FileUtils {

    private static FileUtils mInstance;

    private FileUtils() {}

    public static FileUtils getInstance() {
        if (mInstance == null) {
            synchronized (FileUtils.class) {
                if (mInstance == null) {
                    mInstance = new FileUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 按行读取文件内容
     * @param filePath
     * @return
     */
    public String readFileByLine(String filePath) {
        File file = new File(filePath);
        FileInputStream fis = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();

        try {
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis));

            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getContentByInputStream(InputStream is) {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);

        byte[] bytes = null;
        String content = null;

        byte[] buf = new byte[1024];
        int len = 0;
        try {
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();

            bytes = baos.toByteArray();
            content = new String(bytes, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static void main(String[] args) {
        String url = "https%3A%2F%2Fnest-mobile.faas.ele.me%2Fsetuserid%3FappId%3D58338686%26redirectUrl%3Dhttps%253A%252F%252Fyc-h5.faas.ele.me";
        try {
            String decoder = URLDecoder.decode(url, "utf-8");
            System.out.print(decoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
