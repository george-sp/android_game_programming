package com.codeburrow.platformer;

/**
 * Can be collected from the player - Pickups (extra lives).
 */
public class ExtraLife extends GameObject {

    ExtraLife(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = .8f;
        final float WIDTH = .65f;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        // Choose a Bitmap.
        setBitmapName("life");

        // Set extra life in the world.
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {
    }
}
