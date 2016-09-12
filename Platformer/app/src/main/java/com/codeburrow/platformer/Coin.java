package com.codeburrow.platformer;

/**
 * Can be collected from the player - Pickups (money).
 */
public class Coin extends GameObject {

    Coin(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = .5f;
        final float WIDTH = .5f;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        // Choose a Bitmap.
        setBitmapName("coin");

        // Set coin in the world.
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {
    }

}
