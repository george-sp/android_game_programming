package com.codeburrow.platformer;

import android.content.Context;

/**
 * The hero of the game.
 */
public class Player extends GameObject {

    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMetre) {
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT); // 2 metre tall.
        setWidth(WIDTH); // 1 metre wide.

        // Note that the type is set to 'p'.
        setType('p');

        /*
         * This is a sprite sheet with multiple frames of animation.
         * So it will look silly until we animate it
         */
        setBitmapName("player");

        // The x and y coordinates.
        setWorldLocation(worldStartX, worldStartY, 0);
    }

    public void update(long fps, float gravity) {
    }
}
