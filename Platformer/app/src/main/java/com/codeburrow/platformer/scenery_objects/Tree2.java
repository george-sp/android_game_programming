package com.codeburrow.platformer.scenery_objects;

import com.codeburrow.platformer.game_engine.GameObject;

import java.util.Random;

public class Tree2 extends GameObject {

    public Tree2(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 4;
        final float WIDTH = 2;
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setType(type);
        setBitmapName("tree2");
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
