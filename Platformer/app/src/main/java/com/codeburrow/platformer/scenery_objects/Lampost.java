package com.codeburrow.platformer.scenery_objects;

import com.codeburrow.platformer.game_engine.GameObject;

import java.util.Random;

public class Lampost extends GameObject {

    public Lampost(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 3;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("lampost");
        setActive(false);
        Random rand = new Random();
        if (rand.nextInt(2) == 0) {
            setWorldLocation(worldStartX, worldStartY, -1);
        } else {
            setWorldLocation(worldStartX, worldStartY, 1);
        }
    }

    public void update(long fps, float gravity) {
    }
}