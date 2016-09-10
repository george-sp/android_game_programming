package com.codeburrow.platformer;

import android.content.Context;

/**
 * The hero of the game.
 */
public class Player extends GameObject {

    // Four hit-boxes: head, feet, left hand, right hand
    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;
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

        // Set player standing still to start.
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

        rectHitboxFeet = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();
    }

    public void update(long fps, float gravity) {
        // Set the player's velocity if left or right is pressed.
        if (isPressingRight) {
            this.setxVelocity(MAX_X_VELOCITY);
        } else if (isPressingLeft) {
            this.setxVelocity(-MAX_X_VELOCITY);
        } else {
            this.setxVelocity(0);
        }

        // Check if player is moving and setFacing.
        if (this.getxVelocity() > 0) {
            //facing right
            setFacing(RIGHT);
        } else if (this.getxVelocity() < 0) {
            //facing left
            setFacing(LEFT);
        }

        // Handle jumping and gravity.
        if (isJumping) {
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    this.setyVelocity(-gravity);//on the way up
                } else if (timeJumping > maxJumpTime / 2) {
                    this.setyVelocity(gravity);//going down
                }
            } else {
                isJumping = false;
            }
        } else {
            this.setyVelocity(gravity);
            /*
             * The next line stops the player from being able to jump in mid air.
             * Comment it in to make the game easier.
             */
            isFalling = true;
        }

        // Update the x and y coordinates.
        this.move(fps);

        // Update all the hit-boxes to the new location.
        // Get the current world location of the player and save them as local variables to be used next.
        Vector2Point location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        // Update the player feet hit-box.
        rectHitboxFeet.top = ly + (getHeight() * .95f);
        rectHitboxFeet.left = lx + getWidth() * .2f;
        rectHitboxFeet.bottom = ly + getHeight() * .98f;
        rectHitboxFeet.right = lx + getWidth() * .8f;

        // Update player head hit-box.
        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + (getWidth() * .4f);
        rectHitboxHead.bottom = ly + getHeight() * .2f;
        rectHitboxHead.right = lx + (getWidth() * .6f);

        // Update player left hit-box.
        rectHitboxLeft.top = ly + getHeight() * .2f;
        rectHitboxLeft.left = lx + getWidth() * .2f;
        rectHitboxLeft.bottom = ly + getHeight() * .8f;
        rectHitboxLeft.right = lx + (getWidth() * .3f);

        // Update player right hit-box.
        rectHitboxRight.top = ly + getHeight() * .2f;
        rectHitboxRight.left = lx + (getWidth() * .8f);
        rectHitboxRight.bottom = ly + getHeight() * .8f;
        rectHitboxRight.right = lx + getWidth() * .7f;
    }
}
