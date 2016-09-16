package com.codeburrow.platformer.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Has the functionality necessary to control the scrolling.
 * Makes the backgrounds seem endless by putting the images back to back
 * alternating between the regular image and a reversed image.
 */
public class Background {

    public Bitmap bitmap;
    public Bitmap bitmapReversed;

    public int width;
    public int height;

    public boolean reversedFirst;

    // Controls where we clip the bitmaps each frame.
    public int xClip;
    public float y;
    public float endY;
    public int z;

    public float speed;

    boolean isParallax;

    public Background(Context context, int yPixelsPerMetre, int screenWidth, BackgroundData data) {

        int resID = context.getResources().getIdentifier(data.bitmapName,
                "drawable", context.getPackageName());

        // For parallax.
        bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        // Which version of background (reversed or regular) is currently drawn first (on left).
        reversedFirst = false;

        // Initialise animation variables.
        // Always start at zero.
        xClip = 0;
        y = data.startY;
        endY = data.endY;
        z = data.layer;
        isParallax = data.isParallax;
        // Scrolling background speed.
        speed = data.speed;

        // Scale background to fit the screen resolution.
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, data.height * yPixelsPerMetre, true);

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        // Create a mirror image of the background (horizontal flip).
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        bitmapReversed = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
