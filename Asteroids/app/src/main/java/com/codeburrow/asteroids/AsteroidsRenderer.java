package com.codeburrow.asteroids;

import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import java.util.ArrayList;

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

    // This will hold our game buttons.
    private final GameButton[] gameButtons = new GameButton[5];

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

    /**
     * Handles the death of a ship.
     * Resets the ship's location to the center of the map, play a sound and decrement numLives.
     */
    public void lifeLost() {
        // Reset the ship to the centre.
        gameManager.ship.setWorldLocation(gameManager.mapWidth / 2, gameManager.mapHeight / 2);
        // Play a sound.
        soundManager.playSound("shipexplode");
        // Deduct a life.
        gameManager.numLives = gameManager.numLives - 1;

        if (gameManager.numLives == 0) {
            gameManager.levelNumber = 1;
            gameManager.numLives = 3;
            createObjects();
            gameManager.switchPlayingStatus();
            soundManager.playSound("gameover");
        }
    }

    /**
     * Handles what happen when an asteroid dies.
     *
     * @param asteroidIndex
     */
    public void destroyAsteroid(int asteroidIndex) {
        gameManager.asteroids[asteroidIndex].setActive(false);
        // Play a sound.
        soundManager.playSound("explode");
        // Reduce the number of active asteroids.
        gameManager.numAsteroidsRemaining--;

        // Has the player cleared them all?
        if (gameManager.numAsteroidsRemaining == 0) {
            // Play a victory sound.

            // Increment the level number.
            gameManager.levelNumber++;

            // Extra life.
            gameManager.numLives++;

            soundManager.playSound("nextlevel");
            // Respawn everything with more asteroids.
            createObjects();
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

        // Now for the HUD objects.
        // First the life icons.
        for (int i = 0; i < gameManager.numLives; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gameManager.lifeIcons[i] = new LifeIcon(gameManager, i);
        }


        // Now the tally icons (1 at the start).
        for (int i = 0; i < gameManager.numAsteroidsRemaining; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gameManager.tallyIcons[i] = new TallyIcon(gameManager, i);
        }

        // Now the buttons.
        ArrayList<Rect> buttonsToDraw = inputController.getButtons();
        int i = 0;
        for (Rect rect : buttonsToDraw) {
            gameButtons[i] = new GameButton(rect.top, rect.left, rect.bottom, rect.right, gameManager);
            i++;
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
        // Update all the asteroids.
        for (int i = 0; i < gameManager.numAsteroids; i++) {
            if (gameManager.asteroids[i].isActive()) {
                gameManager.asteroids[i].update(fps);
            }
        }
        // End of all updates!!

        // All objects are in their new locations.
        // Start collision detection.

        // Check if the ship needs containing.
        if (CollisionDetection.contain(gameManager.mapWidth, gameManager.mapHeight, gameManager.ship.collisionPackage)) {
            lifeLost();
        }

        // Check if an asteroid needs containing.
        for (int i = 0; i < gameManager.numAsteroids; i++) {
            if (gameManager.asteroids[i].isActive()) {
                if (CollisionDetection.contain(gameManager.mapWidth, gameManager.mapHeight, gameManager.asteroids[i].collisionPackage)) {
                    // Bounce the asteroid back into the game.
                    gameManager.asteroids[i].bounce();
                    // Play a sound.
                    soundManager.playSound("blip");
                }
            }
        }

        // Check if bullet needs containing.
        // But first see if the bullet is out of sight.
        // If it is reset it to make game harder.
        for (int i = 0; i < gameManager.numBullets; i++) {
            // Is the bullet in flight?
            if (gameManager.bullets[i].isInFlight()) {
                /*
                 * Comment the next block to make the game easier!!!
                 * It will allow the bullets to go all the way from ship to border without being reset.
                 * These lines reset the bullet when shortly after they leave the players view.
                 * This forces the player to go 'hunting' for the asteroids
                 * instead of spinning round spamming the fire button...
                 */

                // Start comment out to make easier.
                handyPointF = gameManager.bullets[i].getWorldLocation();
                handyPointF2 = gameManager.ship.getWorldLocation();
                if (handyPointF.x > handyPointF2.x + gameManager.metresToShowX / 2) {
                    // Reset the bullet.
                    gameManager.bullets[i].resetBullet(gameManager.ship.getWorldLocation());
                } else if (handyPointF.x < handyPointF2.x - gameManager.metresToShowX / 2) {
                    // Reset the bullet.
                    gameManager.bullets[i].resetBullet(gameManager.ship.getWorldLocation());
                } else if (handyPointF.y > handyPointF2.y + gameManager.metresToShowY / 2) {
                    // Reset the bullet.
                    gameManager.bullets[i].resetBullet(gameManager.ship.getWorldLocation());
                } else if (handyPointF.y < handyPointF2.y - gameManager.metresToShowY / 2) {
                    // Reset the bullet.
                    gameManager.bullets[i].resetBullet(gameManager.ship.getWorldLocation());
                }
                // End comment out to make easier

                // Does bullet need containing?
                if (CollisionDetection.contain(gameManager.mapWidth, gameManager.mapHeight, gameManager.bullets[i].collisionPackage)) {
                    // Reset the bullet.
                    gameManager.bullets[i].resetBullet(gameManager.ship.getWorldLocation());
                    // Play a sound.
                    soundManager.playSound("ricochet");
                }
            }
        }

        // Now we see if anything has hit an asteroid.

        // Check collisions between asteroids and bullets.
        // Loop through each bullet and asteroid in turn.
        for (int bulletNum = 0; bulletNum < gameManager.numBullets; bulletNum++) {
            for (int asteroidNum = 0; asteroidNum < gameManager.numAsteroids; asteroidNum++) {
                // Check that the current bullet is in flight
                // and the current asteroid is active before proceeding.
                if (gameManager.bullets[bulletNum].isInFlight() && gameManager.asteroids[asteroidNum].isActive())
                    // Perform the collision checks by passing in the collision packages.

                    // A Bullet only has one vertex.
                    // Our collision detection works on vertex pairs.
                    if (CollisionDetection.detect(gameManager.bullets[bulletNum].collisionPackage, gameManager.asteroids[asteroidNum].collisionPackage)) {
                        // If we get a hit...
                        destroyAsteroid(asteroidNum);
                        // Reset the bullet.
                        gameManager.bullets[bulletNum].resetBullet(gameManager.ship.getWorldLocation());
                    }
            }
        }
        // Check collisions between asteroids and ship.
        // Loop through each asteroid in turn.
        for (int asteroidNum = 0; asteroidNum < gameManager.numAsteroids; asteroidNum++) {
            // Is the current asteroid active before proceeding.
            if (gameManager.asteroids[asteroidNum].isActive()) {
                // Perform the collision checks by passing in the collision packages.
                if (CollisionDetection.detect(gameManager.ship.collisionPackage, gameManager.asteroids[asteroidNum].collisionPackage)) {
                    // Hit!
                    destroyAsteroid(asteroidNum);
                    lifeLost();
                }
            }
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
        // Draw the asteroids.
        for (int i = 0; i < gameManager.numAsteroids; i++) {
            if (gameManager.asteroids[i].isActive()) {
                gameManager.asteroids[i].draw(viewportMatrix);
            }
        }
        // Draw the ship.
        gameManager.ship.draw(viewportMatrix);
        // Draw the border.
        gameManager.border.draw(viewportMatrix);
        // Draw the buttons.
        for (int i = 0; i < gameButtons.length; i++) {
            gameButtons[i].draw();
        }
        // Draw the life icons.
        for (int i = 0; i < gameManager.numLives; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gameManager.lifeIcons[i].draw();
        }
        // Draw the level icons.
        for (int i = 0; i < gameManager.numAsteroidsRemaining; i++) {
            // Notice we send in which icon this represents
            // from left to right so padding and positioning is correct.
            gameManager.tallyIcons[i].draw();
        }
    }
}
