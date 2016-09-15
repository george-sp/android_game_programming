package com.codeburrow.platformer;

import java.util.Random;

public class Boulders extends GameObject {

    Boulders(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1;
        final float WIDTH = 3;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        // Choose a Bitmap.
        setBitmapName("boulder");
        // Don't check it for collisions.
        setActive(false);

        // Randomly set the tree either just in front or just behind the player -1 or 1.
        Random rand = new Random();
        if (rand.nextInt(2) == 0) {
            // -1 drawn before the rest.
            setWorldLocation(worldStartX, worldStartY, -1);
        } else {
            // 1 drawn before the rest.
            setWorldLocation(worldStartX, worldStartY, 1);
        }
        // Do not add a hit-box!!
        //setRectHitbox();
    }

    public void update(long fps, float gravity) {
    }
}
