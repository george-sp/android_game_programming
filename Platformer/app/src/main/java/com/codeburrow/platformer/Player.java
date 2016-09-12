package com.codeburrow.platformer;

import android.content.Context;

/**
 * The hero of the game.
 */
public class Player extends GameObject {

    public MachineGun bfg;

    // Specific required number of frames and frames per second.
    final int ANIMATION_FPS = 16;
    final int ANIMATION_FRAME_COUNT = 5;

    // Four hit-boxes: head, feet, left hand, right hand.
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

        bfg = new MachineGun();

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

        // Set this object up to be animated.
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMetre, true);

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

        // Update the machine-gun.
        bfg.update(fps, gravity);

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

    /**
     * Attempts to fire a shot.
     *
     * @return True, if a shot was fired.
     */
    public boolean pullTrigger() {
        return bfg.shoot(this.getWorldLocation().x, this.getWorldLocation().y, getFacing(), getHeight());
    }

    /**
     * Check for collisions and react to them.
     *
     * @param rectHitbox The RectHitBox of whichever object we are currently checking against for collision.
     * @return An int to represent if there was a collision and where on the player that collision occurred.
     */
    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;//no collision

        // Check the left side.
        if (this.rectHitboxLeft.intersects(rectHitbox)) {
            // Left has collided, move player just to right of current hit-box.
            this.setWorldLocationX(rectHitbox.right - getWidth() * .2f);
            collided = 1;
        }

        // Check the right side.
        if (this.rectHitboxRight.intersects(rectHitbox)) {
            // Right has collided, move player just to left of current hit-box.
            this.setWorldLocationX(rectHitbox.left - getWidth() * .8f);
            collided = 1;
        }

        // Check the feet.
        if (this.rectHitboxFeet.intersects(rectHitbox)) {
            // Feet have collided, move feet to just above current hit-box.
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
        }

        // Check the head.
        if (this.rectHitboxHead.intersects(rectHitbox)) {
            // Head has collided, move head to just below current hitbox bottom
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }

        return collided;
    }

    public void setPressingRight(boolean isPressingRight) {
        this.isPressingRight = isPressingRight;
    }

    public void setPressingLeft(boolean isPressingLeft) {
        this.isPressingLeft = isPressingLeft;
    }

    public void startJump(SoundManager sm) {
        // Can't jump if falling or already jumping.
        if (!isFalling) {
            if (!isJumping) {
                isJumping = true;
                jumpTime = System.currentTimeMillis();
                sm.playSound("jump");
            }
        }
    }
}
