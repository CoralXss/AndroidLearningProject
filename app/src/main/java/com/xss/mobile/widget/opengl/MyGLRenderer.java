package com.xss.mobile.widget.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by xss on 2016/11/4.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;

    // 调用一次，用来配置View的OpenGL ES环境
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        mTriangle = new Triangle();
    }

    // 若View的几何形态发生变化时会被调用，例如当设备的屏幕方向发生改变时
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    // 每次重新绘制View时被调用
    @Override
    public void onDrawFrame(GL10 gl10) {
        // 重绘背景色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
