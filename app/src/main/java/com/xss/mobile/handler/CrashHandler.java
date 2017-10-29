package com.xss.mobile.handler;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.core.util.IOUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xss on 2017/4/5.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /** TAG */
    private static final String TAG = "CrashHandler";
    /** mDefaultHandler */
    private Thread.UncaughtExceptionHandler defaultHandler;

    /** instance */
    private static CrashHandler instance = new CrashHandler();

    /** infos */
    private Map<String, String> infos = new HashMap<String, String>();

    /** formatter */
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean isCrach=false;
    /** context*/
    private Context context;
    private CrashHandler() {}

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     *
     * @param ctx
     * 初始化，此处最好在Application的OnCreate方法里来进行调用
     */
    public void init(Context ctx) {
        this.context = ctx;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * uncaughtException
     * 在这里处理为捕获的Exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handleException(throwable);
        defaultHandler.uncaughtException(thread, throwable);
    }
    private void handleException(Throwable ex) {
        try {
            if (isCrach || ex == null) {
                return;
            }
            Log.d("--------------------TAG", "收到崩溃");


            Log.e("---message---", ex.getMessage());
            Log.e("---localizedMessage---", ex.getLocalizedMessage()); // 同上一个，描述信息
            Log.e("---cause---", ex.getCause().toString());  // 完整信息

            String path = Environment.getDataDirectory() + "/1_crash.txt";
            writeCrashInfoToFile(ex, path);
        }catch (Exception e){

        }
    }


    /**
     *
     * @param ex
     * 将崩溃写入文件系统
     */
    StringBuffer sb;
    private void writeCrashInfoToFile(final Throwable ex,final String localFileUrl) {
        try {

            isCrach=true;
            if(!getFileSizes(localFileUrl)) {
                IOUtil.deleteFileOrDir(new File(localFileUrl));
            }
            String time = formatter.format(new Date());
            sb = new StringBuffer();
            sb.append(time+"  ");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            sb.append(result);

            //这里把刚才异常堆栈信息写入SD卡的Log日志里面
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                writeLog(sb.toString(),localFileUrl);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param log
     * @param name
     * @return 返回写入的文件路径
     * 写入Log信息的方法，写入到SD卡里面
     */
    private String writeLog(String log, String name)
    {

        File file = new File(name);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try
        {
            Log.d("TAG", "写入到SD卡里面");
            file.createNewFile();
            FileWriter fw=new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            //写入相关Log到文件
            bw.write(log);
            bw.newLine();
            bw.close();
            fw.close();
            return name;
        }
        catch (IOException e)
        {
            Log.e(TAG, "an error occured while writing file...", e);
            e.printStackTrace();
            return null;
        }
    }
    /**
     　　* 获取指定文件夹
     　　* @param f
     　　* @return
     　　* @throws Exception
     　　*/
    public boolean getFileSizes(String filePath){
        long size = 0;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            }
            return size / (1024*100) > 0 ? false : true;
        }catch (Exception e){
            return true;
        }
    }
}
