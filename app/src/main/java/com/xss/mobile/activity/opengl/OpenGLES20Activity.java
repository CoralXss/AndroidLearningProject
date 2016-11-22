package com.xss.mobile.activity.opengl;

import android.app.Activity;
import android.os.Bundle;

import com.xss.mobile.widget.opengl.MyGLSurfaceView;

/**
 * Created by xss on 2016/11/4.
 */
public class OpenGLES20Activity extends Activity {
    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

    }
}
