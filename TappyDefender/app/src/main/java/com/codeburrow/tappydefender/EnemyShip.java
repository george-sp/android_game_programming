package com.codeburrow.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    // Constructor
    public EnemyShip(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random random = new Random();
        speed = random.nextInt(6) + 10;

        x = screenX;
        y = random.nextInt(maxY) - bitmap.getHeight();
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
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
