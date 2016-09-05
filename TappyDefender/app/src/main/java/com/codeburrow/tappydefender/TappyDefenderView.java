package com.codeburrow.tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class represents the view of our game. -- A dynamically drawn view.
 */
public class TappyDefenderView extends SurfaceView implements Runnable {

    private static final String LOG_TAG = TappyDefenderView.class.getSimpleName();

    /*
     * volatile:
     * is used to indicate that a variable's value will be modified by different threads.
     */
    volatile boolean playing;
    Thread gameThread = null;

    // Game objects
    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;

    // For drawing
    // A virtual Canvas to draw graphics upon.
    private Canvas canvas;
    // Use a Paint object to add Bitmaps and manipulate individual px on Canvas.
    private Paint paint;
    // Locks the Canvas object while it is manipulated
    // and unlocks it when it is ready to draw the frames.
    private SurfaceHolder surfaceHolder;

    public TappyDefenderView(Context context, int x, int y) {
        super(context);

        // Initialize our drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        // Initialize our player ship
        player = new PlayerShip(context, x, y);
        enemy1 = new EnemyShip(context, x, y);
        enemy2 = new EnemyShip(context, x, y);
        enemy3 = new EnemyShip(context, x, y);
    }

    /**
     * The run method will execute in a thread,
     * but it will only execute the game loop while the boolean playing instance is true.
     */
    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    /**
     * Updates all the game data.
     */
    private void update() {
        // Update the player.
        player.update();
        // Update the enemies.
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());
    }

    /**
     * Draws the screen based on that game data.
     */
    private void draw() {
        // Check that the SurfaceHolder class is valid.
        if (surfaceHolder.getSurface().isValid()) {
            // Lock the area of memory we will be drawing to.
            canvas = surfaceHolder.lockCanvas();
            // Clear the screen from the last frame, with a call to drawColor().
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            // Draw the player.
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);
            // Draw the enemies.
            canvas.drawBitmap
                    (enemy1.getBitmap(),
                            enemy1.getX(),
                            enemy1.getY(), paint);
            canvas.drawBitmap
                    (enemy2.getBitmap(),
                            enemy2.getX(),
                            enemy2.getY(), paint);
            canvas.drawBitmap
                    (enemy3.getBitmap(),
                            enemy3.getX(),
                            enemy3.getY(), paint);
            // Unlock the Canvas object and draw the scene.
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Controls how long it is until the run method is called again.
     */
    private void control() {
        try {
            // Pass the thread for (1000 milliseconds / 60 HPS) approximately 17 millis.
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Has the player lifted there finger up?
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;

            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }
        return true;
    }

    /**
     * Cleans up the thread if the game is interrupted or the player quits.
     */
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Makes a new thread and start it.
     * Execution moves to R.
     */
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
