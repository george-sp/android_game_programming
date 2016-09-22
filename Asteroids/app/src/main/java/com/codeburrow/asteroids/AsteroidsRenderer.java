package com.codeburrow.asteroids;

import android.graphics.PointF;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * This class is attached as the renderer of the GLSurfaceView.
 */
public class AsteroidsRenderer implements Renderer {

    private static final String LOG_TAG = AsteroidsRenderer.class.getSimpleName();
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
    private SoundManager soundManager;
    private InputController inputController;

    // For capturing various PointF details without creating new objects in the speed critical areas.
    PointF handyPointF;
    PointF handyPointF2;

    public AsteroidsRenderer(GameManager gameManager, SoundManager soundManager, InputController inputController) {
        this.gameManager = gameManager;
        this.soundManager = soundManager;
        this.inputController = inputController;

        handyPointF = new PointF();
        handyPointF2 = new PointF();
    }

    /**
     * It is called every time a GLSurfaceView class with attached renderer is created.
     *
     * @param glUnused
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // The color that will be used to clear the screen each frame in onDrawFrame().
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Get GLManager to compile and link the shaders into an object.
        GLManager.buildProgram();
        createObjects();
    }

    /**
     * This next overridden method is called once after onSurfaceCreated()
     * and any time the screen orientation changes.
     *
     * @param glUnused
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Make full screen.
        glViewport(0, 0, width, height);

        /*
            Initialize our viewport matrix by passing in the starting
            range of the game world that will be mapped, by OpenGL to
            the screen. We will dynamically amend this as the player
            moves around.

            The arguments to setup the viewport matrix:
            our array,
            starting index in array,
            min x, max x,
            min y, max y,
            min z, max z)
        */

        orthoM(viewportMatrix, 0, 0, gameManager.metresToShowX, 0, gameManager.metresToShowY, 0f, 1f);
    }

    /**
     * OpenGL will call onDrawFrame() up to hundreds of times per second.
     *
     * @param glUnused
     */
    @Override
    public void onDrawFrame(GL10 glUnused) {
        long startFrameTime = System.currentTimeMillis();

        if (gameManager.isPlaying()) {
            update(fps);
        }

        draw();

        // Calculate the fps this frame.
        // We can then use the result to time animations and more.
        long timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }

        // Output the average frames per second to the console.
        if (debugging) {
            frameCounter++;
            averageFPS = averageFPS + fps;
            if (frameCounter > 100) {
                averageFPS = averageFPS / frameCounter;
                frameCounter = 0;
                Log.e(LOG_TAG, "averageFPS: " + averageFPS);
            }
        }
    }

    private void createObjects() {
        // Create our game objects.

        // First the ship in the center of the map.
        gameManager.ship = new SpaceShip(gameManager.mapWidth / 2, gameManager.mapHeight / 2);
        // The deadly border.
        gameManager.border = new Border(gameManager.mapWidth, gameManager.mapHeight);
        // Some stars.
        gameManager.stars = new Star[gameManager.numStars];
        for (int i = 0; i < gameManager.numStars; i++) {
            // Pass in the map size so the stars no where to spawn.
            gameManager.stars[i] = new Star(gameManager.mapWidth, gameManager.mapHeight);
        }
        // Some bullets.
        gameManager.bullets = new Bullet[gameManager.numBullets];
        for (int i = 0; i < gameManager.numBullets; i++) {
            // Initialize their location in the game world as the center of the ship.
            gameManager.bullets[i] = new Bullet(gameManager.ship.getWorldLocation().x, gameManager.ship.getWorldLocation().y);
        }

        // Determine the number of asteroids.
        gameManager.numAsteroids = gameManager.baseNumAsteroids * gameManager.levelNumber;
        // Set how many asteroids need to be destroyed by player.
        gameManager.numAsteroidsRemaining = gameManager.numAsteroids;
        // Spawn the asteroids.
        //gm.asteroids = new Asteroid[gm.baseNumAsteroids];
        for (int i = 0; i < gameManager.numAsteroids * gameManager.levelNumber; i++) {
            // Create a new asteroid.
            // Pass in level number so they can be made appropriately dangerous.
            gameManager.asteroids[i] = new Asteroid(gameManager.levelNumber, gameManager.mapWidth, gameManager.mapHeight);
        }
    }

    private void update(long fps) {
        // Update the stars. Twinkle the stars.
        for (int i = 0; i < gameManager.numStars; i++) {
            gameManager.stars[i].update();
        }
        // Update the space-ship.
        gameManager.ship.update(fps);
        // Update all the bullets.
        for (int i = 0; i < gameManager.numBullets; i++) {
            gameManager.bullets[i].update(fps, gameManager.ship.getWorldLocation());
        }
    }

    private void draw() {
        // Where is the ship?
        handyPointF = gameManager.ship.getWorldLocation();

        // Modify the viewport matrix orthographic projection based on the ship location.
        orthoM(viewportMatrix, 0,
                handyPointF.x - gameManager.metresToShowX / 2,
                handyPointF.x + gameManager.metresToShowX / 2,
                handyPointF.y - gameManager.metresToShowY / 2,
                handyPointF.y + gameManager.metresToShowY / 2,
                0f, 1f);

        // Clear the screen.
        glClear(GL_COLOR_BUFFER_BIT);

        // Start drawing!

        // Draw some stars.
        for (int i = 0; i < gameManager.numStars; i++) {
            // Draw the star if it is an active one.
            if (gameManager.stars[i].isActive()) {
                gameManager.stars[i].draw(viewportMatrix);
            }
        }
        // Draw the bullets.
        for (int i = 0; i < gameManager.numBullets; i++) {
            gameManager.bullets[i].draw(viewportMatrix);
        }
        // Draw the ship.
        gameManager.ship.draw(viewportMatrix);
        // Draw the border.
        gameManager.border.draw(viewportMatrix);
    }
}
