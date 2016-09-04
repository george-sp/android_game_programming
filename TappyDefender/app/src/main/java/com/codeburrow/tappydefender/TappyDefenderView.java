package com.codeburrow.tappydefender;

import android.content.Context;
import android.util.Log;
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

    public TappyDefenderView(Context context) {
        super(context);
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
    }

    /**
     * Draws the screen based on that game data.
     */
    private void draw() {

    }

    /**
     * Controls how long it is until the run method is called again.
     */
    private void control() {

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
