package com.codeburrow.asteroids;

import android.graphics.PointF;
import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * This class is attached as the renderer of the GLSurfaceView.
 */
public class AsteroidsRenderer implements Renderer {

    boolean debugging = true;

    // For monitoring and controlling the frames per second.
    long frameCounter = 0;
    long averageFPS = 0;
    private long fps;

    // For converting each game world coordinate into a GL space coordinate (-1,-1 to 1,1)
    // for drawing on the screen.
    private final float[] viewportMatrix = new float[16];

    // A class to help manage our game objects current state.
    private GameManager gameManager;

    // For capturing various PointF details without creating new objects in the speed critical areas.
    PointF handyPointF;
    PointF handyPointF2;

    public AsteroidsRenderer(GameManager gameManager) {
        this.gameManager = gameManager;

        handyPointF = new PointF();
        handyPointF2 = new PointF();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }

    private void createObjects() {

    }

    private void update(long fps) {

    }


    private void draw() {

    }
}
