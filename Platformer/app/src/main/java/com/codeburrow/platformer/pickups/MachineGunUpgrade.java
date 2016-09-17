package com.codeburrow.platformer.pickups;

import com.codeburrow.platformer.game_engine.GameObject;

/**
 * Can be collected from the player - Pickups (machine-gun upgrade pack).
 */
public class MachineGunUpgrade extends GameObject {

    public MachineGunUpgrade(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = .5f;
        final float WIDTH = .5f;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        // Choose a Bitmap.
        setBitmapName("clip");

        // Set machine-gun upgrade pack in the world.
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {
    }
}
