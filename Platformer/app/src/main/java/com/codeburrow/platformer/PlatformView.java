package com.codeburrow.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    SoundManager soundManager;

    public PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        // Initialize the drawing objects.
        surfaceHolder = getHolder();
        paint = new Paint();

        // Initialize the viewport.
        viewport = new Viewport(screenWidth, screenHeight);

        soundManager = new SoundManager();
        soundManager.loadSound(context);

        // Load the first level.
        loadLevel("LevelCave", 15, 2);
    }

    public void loadLevel(String level, float px, float py) {
        // Create a new LevelManager.
        levelManager = null;
        levelManager = new LevelManager(context, viewport.getPixelsPerMetreX(), viewport.getScreenWidth(), inputController, level, px, py);
        // Create a new InputController.
        inputController = new InputController(viewport.getScreenWidth(), viewport.getScreenHeight());
        // Set the player's location as the world centre.
        viewport.setWorldCentre(
                levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().x,
                levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y);
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
        for (GameObject go : levelManager.gameObjects) {
            if (go.isActive()) {
                // Clip anything off-screen.
                if (!viewport.clipObjects(go.getWorldLocation().x, go.getWorldLocation().y, go.getWidth(), go.getHeight())) {
                    // Set visible flag to true.
                    go.setVisible(true);
                    if (levelManager.isPlaying()) {
                        // Run any un-clipped updates.
                        go.update(fps, levelManager.gravity);
                    }
                } else {
                    // Set visible flag to false.
                    go.setVisible(false);
                }
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            // Start editing the pixels in the surface.
            canvas = surfaceHolder.lockCanvas();

            // Delete the last frame with arbitrary color.
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));

            // Draw all the GameObjects.
            Rect toScreen2d = new Rect();
            // Draw a layer at a time.
            for (int layer = -1; layer <= 1; layer++)
                // Iterate through the gameObjects ArrayList once for each layer,
                // starting with the lowest layer.
                for (GameObject go : levelManager.gameObjects) {
                    // Check if the object is visible and on the current layer.
                    if (go.isVisible() && go.getWorldLocation().z == layer) {
                        toScreen2d.set(viewport.worldToScreen(
                                go.getWorldLocation().x,
                                go.getWorldLocation().y,
                                go.getWidth(),
                                go.getHeight()));
                        // Draw the appropriate bitmap.
                        canvas.drawBitmap(levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())], toScreen2d.left, toScreen2d.top, paint);
                    }
                }

            // Draw some debugging info.
            if (debugging) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawText("fps:" + fps, 10, 60, paint);
                canvas.drawText("num objects:" + levelManager.gameObjects.size(), 10, 80, paint);
                canvas.drawText("num clipped:" + viewport.getNumClipped(), 10, 100, paint);
                canvas.drawText("playerX:" + levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().x, 10, 120, paint);
                canvas.drawText("playerY:" + levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y, 10, 140, paint);
                canvas.drawText("Gravity:" + levelManager.gravity, 10, 160, paint);
                canvas.drawText("X velocity:" + levelManager.gameObjects.get(levelManager.playerIndex).getxVelocity(), 10, 180, paint);
                canvas.drawText("Y velocity:" + levelManager.gameObjects.get(levelManager.playerIndex).getyVelocity(), 10, 200, paint);
                // Reset the number of clipped objects.
                viewport.resetNumClipped();
            }

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
