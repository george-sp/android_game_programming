package com.codeburrow.platformer.game_engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.codeburrow.platformer.background.Background;
import com.codeburrow.platformer.enemy.Drone;
import com.codeburrow.platformer.player.PlayerState;

import java.util.ArrayList;

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
    private PlayerState playerState;

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

        playerState = new PlayerState();

        // Load the first level.
        loadLevel("LevelCave", 15, 2);
    }

    public void loadLevel(String level, float px, float py) {
        // Create a new LevelManager.
        levelManager = null;
        levelManager = new LevelManager(context, viewport.getPixelsPerMetreX(), viewport.getScreenWidth(), inputController, level, px, py);
        // Create a new InputController.
        inputController = new InputController(viewport.getScreenWidth(), viewport.getScreenHeight());
        // Store the player starting(respawn) location.
        PointF respawnLocation = new PointF(px, py);
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

                    // Check collisions with player.
                    int hit = levelManager.player.checkCollisions(go.getHitbox());
                    if (hit > 0) {
                        // Collision detected! Now deal with different types.
                        switch (go.getType()) {
                            case 'c':
                                soundManager.playSound("coin_pickup");
                                go.setActive(false);
                                go.setVisible(false);
                                playerState.gotCredit();
                                if (hit != 2) {
                                    // Any hit except feet.
                                    // Now restore velocity that was removed by collision detection.
                                    levelManager.player.restorePreviousVelocity();
                                }
                                break;
                            case 'u':
                                soundManager.playSound("gun_upgrade");
                                go.setActive(false);
                                go.setVisible(false);
                                levelManager.player.machineGun.upgradeRateOfFire();
                                if (hit != 2) {
                                    // Any hit except feet.
                                    // Now restore velocity that was removed by collision detection.
                                    levelManager.player.restorePreviousVelocity();
                                }
                                break;
                            case 'e':
                                go.setActive(false);
                                go.setVisible(false);
                                soundManager.playSound("extra_life");
                                playerState.addLife();
                                if (hit != 2) {
                                    // Any hit except feet.
                                    // Now restore velocity that was removed by collision detection.
                                    levelManager.player.restorePreviousVelocity();
                                }
                                break;
                            case 'd':
                                // Hit by drone.
                                PointF location;
                                soundManager.playSound("player_burn");
                                playerState.loseLife();
                                location = new PointF(playerState.loadLocation().x, playerState.loadLocation().y);
                                levelManager.player.setWorldLocationX(location.x);
                                levelManager.player.setWorldLocationY(location.y);
                                levelManager.player.setxVelocity(0);
                                break;
                            case 'g':
                                // Hit by guard.
                                soundManager.playSound("player_burn");
                                playerState.loseLife();
                                location = new PointF(playerState.loadLocation().x, playerState.loadLocation().y);
                                levelManager.player.setWorldLocationX(location.x);
                                levelManager.player.setWorldLocationY(location.y);
                                levelManager.player.setxVelocity(0);
                                break;
                            case 'f':
                                soundManager.playSound("player_burn");
                                playerState.loseLife();
                                location = new PointF(playerState.loadLocation().x, playerState.loadLocation().y);
                                levelManager.player.setWorldLocationX(location.x);
                                levelManager.player.setWorldLocationY(location.y);
                                levelManager.player.setxVelocity(0);
                                break;
                            default:
                                // Probably a regular tile.
                                if (hit == 1) {
                                    // Left or right have collided.
                                    levelManager.player.setxVelocity(0);
                                    levelManager.player.setPressingRight(false);
                                }
                                if (hit == 2) {
                                    // Feet have collided.
                                    // Enables the player to jump.
                                    levelManager.player.isFalling = false;
                                }
                                break;
                        }
                    }

                    // Check bullet collisions.
                    for (int i = 0; i < levelManager.player.machineGun.getNumBullets(); i++) {
                        //Make a hit-box out of the the current bullet.
                        RectHitbox r = new RectHitbox();
                        r.setLeft(levelManager.player.machineGun.getBulletX(i));
                        r.setTop(levelManager.player.machineGun.getBulletY(i));
                        r.setRight(levelManager.player.machineGun.getBulletX(i) + .1f);
                        r.setBottom(levelManager.player.machineGun.getBulletY(i) + .1f);

                        if (go.getHitbox().intersects(r)) {
                            /*
                             * Collision detected:
                             * make bullet disappear until it is respawned as a new bullet
                             */
                            levelManager.player.machineGun.hideBullet(i);
                            // Now respond depending upon the type of object hit.
                            if (go.getType() != 'g' && go.getType() != 'd') {
                                soundManager.playSound("ricochet");
                            } else if (go.getType() == 'g') {
                                // Knock the guard back.
                                go.setWorldLocationX(go.getWorldLocation().x + 2 * (levelManager.player.machineGun.getDirection(i)));
                                soundManager.playSound("hit_guard");
                            } else if (go.getType() == 'd') {
                                // Destroy the droid.
                                soundManager.playSound("explode");
                                // Permanently clip this drone.
                                go.setWorldLocation(-100, -100, 0);
                            }
                        }
                    }

                    if (levelManager.isPlaying()) {
                        // Run any un-clipped updates.
                        go.update(fps, levelManager.gravity);

                        if (go.getType() == 'd') {
                            // Let any nearby drones know where the player is.
                            Drone d = (Drone) go;
                            d.setWaypoint(levelManager.player.getWorldLocation());
                        }
                    }
                } else {
                    // Set visible flag to false.
                    go.setVisible(false);
                }
            }
        }

        if (levelManager.isPlaying()) {
            // Reset the players location as the centre of the viewport.
            viewport.setWorldCentre(
                    levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().x,
                    levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y);
        }

        // Has player fallen out of the map?
        if (levelManager.player.getWorldLocation().x < 0 ||
                levelManager.player.getWorldLocation().x > levelManager.mapWidth ||
                levelManager.player.getWorldLocation().y > levelManager.mapHeight) {
            soundManager.playSound("player_burn");
            playerState.loseLife();
            PointF location = new PointF(playerState.loadLocation().x, playerState.loadLocation().y);
            levelManager.player.setWorldLocationX(location.x);
            levelManager.player.setWorldLocationY(location.y);
            levelManager.player.setxVelocity(0);
        }

        // Check if game is over.
        if (playerState.getLives() == 0) {
            playerState = new PlayerState();
            loadLevel("LevelCave", 1, 16);
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            // Start editing the pixels in the surface.
            canvas = surfaceHolder.lockCanvas();

            // Delete the last frame with arbitrary color.
            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));

            // Draw parallax backgrounds from -1 to -3.
            drawBackground(0, -3);

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

                        if (go.isAnimated()) {
                            // Get the next frame of the bitmap.
                            if (go.getFacing() == 1) {
                                // Rotate if necessary.
                                // We could pre-compute this during load level.
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect rect = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap bitmap = Bitmap.createBitmap(levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())],
                                        rect.left,
                                        rect.top,
                                        rect.width(),
                                        rect.height(),
                                        flipper,
                                        true);

                                canvas.drawBitmap(bitmap, toScreen2d.left, toScreen2d.top, paint);
                            } else {
                                // Draw it the regular way round.
                                canvas.drawBitmap(levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())], go.getRectToDraw(System.currentTimeMillis()), toScreen2d, paint);
                            }
                        } else {
                            // Just draw the whole bitmap.
                            canvas.drawBitmap(levelManager.bitmapsArray[levelManager.getBitmapIndex(go.getType())], toScreen2d.left, toScreen2d.top, paint);
                        }
                    }
                }

            // Draw the bullets.
            paint.setColor(Color.argb(255, 255, 255, 255));
            for (int i = 0; i < levelManager.player.machineGun.getNumBullets(); i++) {
                // Pass in the x and y coordinates as usual
                // & .25f and .05f for the bullet width and height.
                toScreen2d.set(viewport.worldToScreen
                        (levelManager.player.machineGun.getBulletX(i),
                                levelManager.player.machineGun.getBulletY(i),
                                .25f,
                                .05f));
                canvas.drawRect(toScreen2d, paint);
            }

            // Draw parallax backgrounds from layer 1 to 3.
            drawBackground(4, 0);

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

            // Draw the buttons.
            paint.setColor(Color.argb(80, 255, 255, 255));
            ArrayList<Rect> buttonsToDraw;
            buttonsToDraw = inputController.getButtons();

            for (Rect rect : buttonsToDraw) {
                RectF rf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
                canvas.drawRoundRect(rf, 15f, 15f, paint);
            }

            // Draw the paused text.
            if (!this.levelManager.isPlaying()) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(120);
                canvas.drawText("Paused", viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, paint);
            }

            // Finish editing pixels in the surface.
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawBackground(int start, int stop) {
        // Hold the start and end points of bitmap and reversedBitmap.
        Rect fromRect1 = new Rect();
        Rect toRect1 = new Rect();
        Rect fromRect2 = new Rect();
        Rect toRect2 = new Rect();

        /*
         * Loop through all our backgrounds using the start and stop parameters
         * to decide which backgrounds have a z layer
         * that we are currently interested in drawing.
         */
        for (Background bg : levelManager.backgrounds) {
            if (bg.z < start && bg.z > stop) {
                // Is this layer in the viewport?
                // Clip anything off-screen.
                if (!viewport.clipObjects(-1, bg.y, 1000, bg.height)) {
                    float floatstartY = ((viewport.getyCentre() - ((viewport.getViewportWorldCentreY() - bg.y) * viewport.getPixelsPerMetreY())));
                    int startY = (int) floatstartY;

                    float floatendY = ((viewport.getyCentre() - ((viewport.getViewportWorldCentreY() - bg.endY) * viewport.getPixelsPerMetreY())));
                    int endY = (int) floatendY;

                    // Define what portion of bitmaps to capture and what coordinates to draw them at.
                    fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
                    toRect1 = new Rect(bg.xClip, startY, bg.width, endY);

                    fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
                    toRect2 = new Rect(0, startY, bg.xClip, endY);
                }

                // Draw backgrounds.
                if (!bg.reversedFirst) {

                    canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
                    canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
                } else {
                    canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
                    canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
                }

                // Scroll along based on player's speed and direction.
                bg.xClip -= levelManager.player.getxVelocity() / (20 / bg.speed);
                // If xClip has reached the end of the current first background,
                // simply set xClip to zero and change which bitmap is shown first.
                if (bg.xClip >= bg.width) {
                    bg.xClip = 0;
                    bg.reversedFirst = !bg.reversedFirst;
                } else if (bg.xClip <= 0) {
                    bg.xClip = bg.width;
                    bg.reversedFirst = !bg.reversedFirst;

                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (levelManager != null) {
            inputController.handleInput(motionEvent, levelManager, soundManager, viewport);
        }
        //invalidate();
        return true;
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
