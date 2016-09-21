package com.codeburrow.asteroids;

import android.graphics.PointF;

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

    /**
     * Sets the facingAngle of the bullet to that of the ship.
     * This will cause the bullet to move in the direction the ship was facing
     * at the time the fire button was pressed.
     *
     * @param shipFacingAngle
     */
    public void shoot(float shipFacingAngle) {
        setFacingAngle(shipFacingAngle);
        inFlight = true;
        setSpeed(300);
    }

    /**
     * Sets the bullet inside the ship and cancels its velocity and speed.
     * The bullet will sit invisibly inside the ship until it is fired.
     *
     * @param shipLocation
     */
    public void resetBullet(PointF shipLocation) {
        // Remove the velocity if bullet out of bounds.
        inFlight = false;
        setxVelocity(0);
        setyVelocity(0);
        setSpeed(0);
        setWorldLocation(shipLocation.x, shipLocation.y);
    }

    public boolean isInFlight() {
        return inFlight;
    }
}
