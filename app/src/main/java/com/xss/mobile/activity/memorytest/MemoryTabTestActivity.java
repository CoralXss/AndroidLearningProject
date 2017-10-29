package com.xss.mobile.activity.memorytest;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xss.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过 Memory工具分析是否存在内存泄漏
 */
public class MemoryTabTestActivity extends AppCompatActivity {
    private String TAG = MemoryTabTestActivity.class.getSimpleName();

    private List<View> list = new ArrayList<>();

    private static MemoryLeak memoryLeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_tab_test);

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.e("MemoryAllocated", "memorySize = " + memoryClass);

//        if (memoryLeak == null) {
//            memoryLeak = new MemoryLeak();
//        }

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 10000; i++) {
                    list.add(new ImageView(MemoryTabTestActivity.this));
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Log.e(TAG, "Thread is running !");
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    /*
        init:  A = 3.88M F = 4.25M
        goto:  A = 4.35M F = 3.78M
        click: A = 14.16M F = 7.50M
        click: A = 25.92M F = 16.00M
        click: A = 37.94M F = 3.97M
     */

    class MemoryLeak {

        public void doSomething() {
            Log.e("memoryLeak", "has leaked !");
        }
    }
}
