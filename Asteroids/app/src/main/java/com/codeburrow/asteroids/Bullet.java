package com.codeburrow.asteroids;

/**
 *
 */
public class Bullet extends GameObject {

    private boolean inFlight = false;

    public Bullet(float shipX, float shipY) {
        super();

        setType(Type.BULLET);
        setWorldLocation(shipX, shipY);

        // Define the bullet as a single point
        // in exactly the coordinates as its world location.
        float[] bulletVertices = new float[]{0, 0, 0};

        setVertices(bulletVertices);
    }

}
