package com.codeburrow.platformer.platform_tiles;

import com.codeburrow.platformer.game_engine.GameObject;

/**
 * A basic tile the player can walk on.
 */
public class Grass extends GameObject {

    public Grass(float worldStartX, float worldStartY, char type) {
        setTraversable();

        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT); // 1 metre tall.
        setWidth(WIDTH); // 1 metre wide.

        setType(type);

        // Choose a Bitmap.
        setBitmapName("turf");

        // Where does the tile start: x and y locations from constructor parameters.
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {
    }
}
