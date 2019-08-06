package com.xss.mobile.hook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xss.mobile.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HookClickListenerActivity extends AppCompatActivity {
    String TAG = HookClickListenerActivity.class.getSimpleName();

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook_click_listener);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "button click .");
            }
        });
        hookListener(button);

    }

    private void hookListener(View view) {
        try {
            Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getListenerInfo.setAccessible(true);
            // 反射拿到 ListenerInfo 对象
            Object listenerInfo = getListenerInfo.invoke(view);

            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field mOnClickListener = listenerInfoClz.getDeclaredField("mOnClickListener");
            mOnClickListener.setAccessible(true);
            View.OnClickListener originClickListener = (View.OnClickListener) mOnClickListener.get(listenerInfo);

            // 用自定义的替换原始的 OnClickListener
            View.OnClickListener hookClickListener = new HookListener(originClickListener);
            mOnClickListener.set(listenerInfo, hookClickListener);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    class HookListener implements View.OnClickListener {

        private View.OnClickListener mListener;

        public HookListener(View.OnClickListener originListener) {
            this.mListener = originListener;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(HookClickListenerActivity.this, "Hook Click", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "before click, do what you want to do .");
            if (mListener != null) {
                mListener.onClick(v);
            }
            Log.e(TAG, "after click, do what you want to do .");
        }
    }

}
