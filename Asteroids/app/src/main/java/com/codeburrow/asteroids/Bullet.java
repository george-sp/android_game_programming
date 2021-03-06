package com.codeburrow.asteroids;

import android.graphics.PointF;

/**
 *
 */
public class Bullet extends GameObject {

    private boolean inFlight = false;
    CollisionPackage collisionPackage;

    public Bullet(float shipX, float shipY) {
        super();

        setType(Type.BULLET);
        setWorldLocation(shipX, shipY);

        // Define the bullet as a single point
        // in exactly the coordinates as its world location.
        float[] bulletVertices = new float[]{0, 0, 0};

        setVertices(bulletVertices);

        // Initialize the collision package.
        // (the object space vertex list, x any world location the largest possible radius, facingAngle)

        // First, build a one element array.
        PointF point = new PointF(0, 0);
        PointF[] points = new PointF[1];
        points[0] = point;
        // 1.0f is an approximate representation of the size of a bullet.
        collisionPackage = new CollisionPackage(points, getWorldLocation(), 1.0f, getFacingAngle());
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

    /**
     * Moves the bullet based on its facingAngle and speed,
     * if is inFlight is true.
     *
     * @param fps
     * @param shipLocation
     */
    public void update(long fps, PointF shipLocation) {
        // Set the velocity if bullet inFlight is true.
        if (inFlight) {
            setxVelocity((float) (getSpeed() * Math.cos(Math.toRadians(getFacingAngle() + 90))));
            setyVelocity((float) (getSpeed() * Math.sin(Math.toRadians(getFacingAngle() + 90))));
        } else {
            // Have it sit inside the ship.
            setWorldLocation(shipLocation.x, shipLocation.y);
        }

        move(fps);

        // Update the collision package.
        collisionPackage.facingAngle = getFacingAngle();
        collisionPackage.worldLocation = getWorldLocation();
    }
}
