package com.codeburrow.platformer;

import android.content.Context;

/**
 * The hero of the game.
 */
public class Player extends GameObject {

    // How fast the player can move.
    final float MAX_X_VELOCITY = 10;
    // Indicate if the player is moving to the left or right, falling or jumping.
    boolean isPressingRight = false;
    boolean isPressingLeft = false;
    public boolean isFalling;
    private boolean isJumping;
    // How long it has been jumping.
    private long jumpTime;
    // Maximum jump time is set to 7 10ths of second half up half down.
    private long maxJumpTime = 700;
    // The horizontal and vertical speed.
    private float xVelocity;
    private float yVelocity;
    // The direction the player object is facing.
    final int LEFT = -1;
    final int RIGHT = 1;
    private int facing;
    // The ability to move.
    private boolean moves = false;


    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMetre) {
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT); // 2 metre tall.
        setWidth(WIDTH); // 1 metre wide.

        // Set player standing to start.
        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);
        isFalling = false;
        // Set up the player's other attributes.
        setMoves(true);
        setActive(true);
        setVisible(true);

        // Note that the type is set to 'p'.
        setType('p');

        /*
         * This is a sprite sheet with multiple frames of animation.
         * So it will look silly until we animate it
         */
        setBitmapName("player");

        // The x and y coordinates.
        setWorldLocation(worldStartX, worldStartY, 0);
    }

    public void update(long fps, float gravity) {
    }
}
