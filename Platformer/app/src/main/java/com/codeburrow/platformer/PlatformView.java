package com.codeburrow.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The PlatformActivity's view == The game's view.
 */
public class PlatformView extends SurfaceView implements Runnable {

    private static final String LOG_TAG = PlatformView.class.getSimpleName();

    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    private LevelManager levelManager;
    private Viewport viewport;
    InputController inputController;

    public PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        // Initialize the drawing objects.
        surfaceHolder = getHolder();
        paint = new Paint();
    }

    @Override
    public void run() {
        while (running) {
            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            // Measure how many milliseconds have elapsed since the frame started.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                // Calculate the number of frames the game ran at, in the last frame.
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {

    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            // Start editing the pixels in the surface.
            canvas = surfaceHolder.lockCanvas();

            // Delete the last frame with arbitrary color.
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));

            // Finish editing pixels in the surface.
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            // Wait for this thread to die.
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void resume() {
        running = true;
        // Allocate and execute a new Thread object.
        gameThread = new Thread(this);
        gameThread.start();
    }

}
