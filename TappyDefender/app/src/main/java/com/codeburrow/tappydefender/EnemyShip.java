package com.codeburrow.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * A class for the enemy's spaceship.
 */
public class EnemyShip {

    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;

    // Detect enemies leaving the screen.
    private int maxX;
    private int minX;

    // Spawn enemies within screen bounds.
    private int maxY;
    private int minY;

    // A hit box for collision detection.
    private Rect hitBox;

    // Constructor
    public EnemyShip(Context context, int screenX, int screenY) {
        Random random = new Random();
        int whichBitmap = random.nextInt(3);
        switch (whichBitmap) {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
                break;
        }

        scaleBitmap(screenX);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        speed = random.nextInt(6) + 10;

        x = screenX;
        y = random.nextInt(maxY) - bitmap.getHeight();

        // Initialize the hit box.
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;

        // Respawn when the right-hand edge of the enemy bitmap has disappeared
        // from the left-hand side of the screen.
        if (x < minX - bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        // Refresh hit box location.
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    /**
     * Uses the resolution and the static createScaledBitmap method
     * to reduce our Bitmap objects on a scale of 2 or 3
     * depending on the resolution of the screen.
     *
     * @param screenX The horizontal resolution of the screen.
     */
    public void scaleBitmap(int screenX) {
        if (screenX < 1000) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth() / 3,
                    bitmap.getHeight() / 3,
                    false);
        } else if (screenX < 1200) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2,
                    false);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Rect getHitbox() {
        return hitBox;
    }
}
