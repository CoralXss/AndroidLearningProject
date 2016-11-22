package com.xss.mobile.widget.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by xss on 2016/11/4.
 * desc: 在GLSurfaceView中可以绘制OpenGL ES图形，但绘制对象的任务由Renderer完成
 */
public class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        setRenderer(mRenderer);
    }
}
