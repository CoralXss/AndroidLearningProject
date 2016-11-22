package com.xss.mobile.widget.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by xss on 2016/11/4.
 */
public class Triangle {
    private FloatBuffer vertexBuffer;

    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            0.0f, 0.622008459f, 0.0f,
            -0.5f, -0.311004243f, 0.0f,
            0.5f, -0.311004243f, 0.0f
    };

    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public Triangle() {
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
    }
}
