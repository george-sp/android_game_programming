package com.codeburrow.tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

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
    private boolean gameEnded;
    Thread gameThread = null;

    private Context context;

    private int screenX;
    private int screenY;

    // Data for the HUD
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    // Game objects
    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;
    // Space dust
    ArrayList<SpaceDust> dustList = new ArrayList<>();

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

        this.context = context;

        screenX = x;
        screenY = y;

        // Initialize our drawing objects.
        surfaceHolder = getHolder();
        paint = new Paint();

        startGame();
    }

    private void startGame() {
        // Initialize our player ship.
        player = new PlayerShip(context, screenX, screenY);
        // Initialize the enemy ships.
        enemy1 = new EnemyShip(context, screenX, screenY);
        enemy2 = new EnemyShip(context, screenX, screenY);
        enemy3 = new EnemyShip(context, screenX, screenY);

        // Make some random space dust.
        int numSpecs = 50;
        for (int i = 0; i < numSpecs; i++) {
            // Where will the dust spawn?
            SpaceDust spaceDust = new SpaceDust(screenX, screenY);
            dustList.add(spaceDust);
        }

        // Reset taken time and remaining distance.
        distanceRemaining = 10000;
        timeTaken = 0;

        // Get the current time.
        timeStarted = System.currentTimeMillis();

        // Initialize the boolean gameEnded.
        gameEnded = false;
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
        /*
         * Collision detection on new positions.
         * Before move because we are testing last frames position which has just been drawn.
         *
         * If you are using images in excess of 100 pixels wide
         * then increase the -100 value accordingly.
         */
        boolean hitDetected = false;
        if (Rect.intersects(player.getHitbox(), enemy1.getHitbox())) {
            enemy1.setX(-enemy1.getBitmap().getWidth());
            hitDetected = true;
        }
        if (Rect.intersects(player.getHitbox(), enemy2.getHitbox())) {
            enemy2.setX(-enemy2.getBitmap().getWidth());
            hitDetected = true;
        }
        if (Rect.intersects(player.getHitbox(), enemy3.getHitbox())) {
            enemy3.setX(-enemy3.getBitmap().getWidth());
            hitDetected = true;
        }

        if (hitDetected) {
            player.reduceShieldStrength();
            if (player.getShieldStrength() < 0) {
                //game over so do something
            }
        }

        // Update the player.
        player.update();
        // Update the enemies.
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());

        for (SpaceDust spaceDust : dustList) {
            spaceDust.update(player.getSpeed());
        }

        if (!gameEnded) {
            // Subtract distance to home planet based on current speed.
            distanceRemaining -= player.getSpeed();

            // How long has the player been flying.
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        // Game Over. Player lost all his shields.
        if (hitDetected) {
            player.reduceShieldStrength();
            if (player.getShieldStrength() < 0) {
                gameEnded = true;
            }
        }

        // Completed the game! Player won!
        if (distanceRemaining < 0) {
            // Check for new fastest time.
            if (timeTaken < fastestTime) {
                fastestTime = timeTaken;
            }

            // Avoid ugly negative numbers in the HUD.
            distanceRemaining = 0;

            // Now end the game.
            gameEnded = true;
        }
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
            if (MainActivity.DEBUGGING) {
                // Draw Hit boxes
                canvas.drawRect(player.getHitbox().left,
                        player.getHitbox().top,
                        player.getHitbox().right,
                        player.getHitbox().bottom,
                        paint);
                canvas.drawRect(enemy1.getHitbox().left,
                        enemy1.getHitbox().top,
                        enemy1.getHitbox().right,
                        enemy1.getHitbox().bottom,
                        paint);
                canvas.drawRect(enemy2.getHitbox().left,
                        enemy2.getHitbox().top,
                        enemy2.getHitbox().right,
                        enemy2.getHitbox().bottom,
                        paint);
                canvas.drawRect(enemy3.getHitbox().left,
                        enemy3.getHitbox().top,
                        enemy3.getHitbox().right,
                        enemy3.getHitbox().bottom,
                        paint);
            }
            // White specs of dust.
            paint.setColor(Color.argb(255, 255, 255, 255));
            //Draw the dust from our arrayList.
            for (SpaceDust spaceDust : dustList) {
                canvas.drawPoint(spaceDust.getX(), spaceDust.getY(), paint);
            }
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

            if (!gameEnded) {
                // Draw the HUD.
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawText("Fastest:" + fastestTime + "s", 10, 20, paint);
                canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20, paint);
                canvas.drawText("Distance:" + distanceRemaining / 1000 + " KM", screenX / 3, screenY - 60, paint);
                canvas.drawText("Shield:" + player.getShieldStrength(), 10, screenY - 60, paint);
                canvas.drawText("Speed:" + player.getSpeed() * 60 + " MPS", (screenX / 3) * 2, screenY - 60, paint);
            } else {
                // This happens the game is ended.
                // Show pause screen.
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX / 2, 100, paint);
                paint.setTextSize(25);
                canvas.drawText("Fastest:" +
                        fastestTime + "s", screenX / 2, 160, paint);
                canvas.drawText("Time:" + timeTaken +
                        "s", screenX / 2, 200, paint);
                canvas.drawText("Distance remaining:" +
                        distanceRemaining / 1000 + " KM", screenX / 2, 240, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX / 2, 350, paint);
            }

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
