package com.codeburrow.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A class for the player's spaceship.
 */
public class PlayerShip {

    private Bitmap bitmap;
    private int x, y;
    private int speed;

    private boolean boosting;

    private final int GRAVITY = -12;

    // Stop ship leaving the screen.
    private int maxY;
    private int minY;
    // Limit the ship's speed.
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    // Constructor
    public PlayerShip(Context context, int screenX, int screenY) {
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        maxY = screenY - bitmap.getHeight();
        minY = 0;
    }

    public void update() {
        // Handle ship's speed.
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }

        // Constrain its top speed.
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        // Never stop it completely.
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        // Move the sheep up or down.
        y -= speed + GRAVITY;

        // Do not let it stray off screen.
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }
    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    public Bitmap getBitmap() {

        return bitmap;
    }

    public int getSpeed() {

        return speed;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }
}
