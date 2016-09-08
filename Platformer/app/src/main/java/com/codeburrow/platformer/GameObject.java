package com.codeburrow.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Manages all the game objects: enemies, props, terrain types.
 * <p/>
 * (Can be extended by all different game objects.)
 */
public abstract class GameObject {

    private Vector2Point worldLocation;
    private float width;
    private float height;

    private boolean active = true;
    private boolean visible = true;
    // The number of frames of internal animation the object has.
    private int animFrameCount = 1;
    // Determines exactly what this GameObject might be.
    private char type;

    private String bitmapName;

    public abstract void update(long fps, float gravity);

    public String getBitmapName() {
        return bitmapName;
    }

    /**
     * Resizes the object's Bitmap appropriately.
     *
     * @param context
     * @param bitmapName
     * @param pixelsPerMetre
     * @return
     */
    public Bitmap prepareBitmap(Context context, String bitmapName, int pixelsPerMetre) {
        // Create a resource ID from the bitmapName.
        int resourceID = context.getResources().getIdentifier(bitmapName, "drawable", context.getPackageName());
        // Create the Bitmap.
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID);
        // Scale the bitmap based on the pixels per metre.
        // Also multiply the width by the number of frames in the image (Default is 1).
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (width * animFrameCount * pixelsPerMetre),
                (int) (height * pixelsPerMetre),
                false);
        return bitmap;
    }

    public Vector2Point getWorldLocation() {
        return worldLocation;
    }

    public void setWorldLocation(float x, float y, int z) {
        this.worldLocation = new Vector2Point();
        this.worldLocation.x = x;
        this.worldLocation.y = y;
        this.worldLocation.z = z;
    }

    public void setBitmapName(String bitmapName) {
        this.bitmapName = bitmapName;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
}
