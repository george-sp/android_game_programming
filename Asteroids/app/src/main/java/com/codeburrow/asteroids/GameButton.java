package com.codeburrow.asteroids;

import android.graphics.PointF;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.Matrix.orthoM;
import static com.codeburrow.asteroids.GLManager.FLOAT_SIZE;

public class GameButton {

    // For button coordinate
    // into a GL space coordinate (-1,-1 to 1,1)
    // for drawing on the screen
    private final float[] viewportMatrix = new float[16];

    // A handle to the GL glProgram -
    // the compiled and linked shaders
    private static int glProgram;

    // Each of the above constants also has a matching int
    // which will represent its location in the open GL glProgram
    // In GameButton they are declared as local variables

    // How many vertices does it take to make
    // our button
    private int numVertices;

    // This will hold our vertex data that is
    // passed into openGL glProgram
    // private final FloatBuffer vertices;
    private FloatBuffer vertices;

    public GameButton(int top, int left, int bottom, int right, GameManager gm) {
        // The HUD needs its own viewport notice we set the screen height in pixels
        // as the starting y coordinates because OpenGL is upside down world :-)
        orthoM(viewportMatrix, 0, 0, gm.screenWidth, gm.screenHeight, 0, 0f, 1f);

        // Shrink the button visuals to make them less obtrusive while leaving
        // the screen area they represent the same.
        int width = (right - left) / 2;
        int height = (top - bottom) / 2;
        left = left + width / 2;
        right = right - width / 2;
        top = top - height / 2;
        bottom = bottom + height / 2;

        PointF p1 = new PointF();
        p1.x = left;
        p1.y = top;

        PointF p2 = new PointF();
        p2.x = right;
        p2.y = top;

        PointF p3 = new PointF();
        p3.x = right;
        p3.y = bottom;

        PointF p4 = new PointF();
        p4.x = left;
        p4.y = bottom;

        /*
         * Add the four points to an array of vertices.
         * This time, because we don't need to animate the border
         * we can just declare the world space coordinates, the same as above.
         */
        float[] modelVertices = new float[]{
                // A line from point 1 to point 2
                p1.x, p1.y, 0,
                p2.x, p2.y, 0,
                // Point 2 to point 3
                p2.x, p2.y, 0,
                p3.x, p3.y, 0,
                // Point 3 to point 4
                p3.x, p3.y, 0,
                p4.x, p4.y, 0,
                // Point 4 to point 1
                p4.x, p4.y, 0,
                p1.x, p1.y, 0
        };

        // Store how many vertices and elements there is for future use.
        final int ELEMENTS_PER_VERTEX = 3;// x,y,z
        int numElements = modelVertices.length;
        numVertices = numElements / ELEMENTS_PER_VERTEX;

        /*
         * Initialize the vertices ByteBuffer object based on the
         * number of vertices in the button and the number of
         * bytes there are in the float type
         */
        vertices = ByteBuffer.allocateDirect(numElements * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Add the button into the ByteBuffer object.
        vertices.put(modelVertices);

        glProgram = GLManager.getGLProgram();
    }

}
