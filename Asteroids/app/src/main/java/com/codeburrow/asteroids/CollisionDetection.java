package com.codeburrow.asteroids;

import android.graphics.PointF;

/**
 * Collision Detection:
 * The algorithms we will use are computationally expensive,
 * and we will only want to use them when there is a realistic chance of a collision.
 * <p>
 * Check:
 * - each bullet and the ship against every asteroid using the radius overlapping method.
 * - the asteroids, ship, and bullets against the border using a simplified rectangle intersection method.
 */
public class CollisionDetection {

    private static final String LOG_TAG = CollisionDetection.class.getSimpleName();
    private static PointF rotatedPoint = new PointF();

    /**
     * Detects collisions between bullets and the asteroids as well as the ship and asteroids.
     * <p>
     * The code works by making a hypothetical triangle with a missing side,
     * and then using Pythagoras' theorem to calculate the missing side
     * that is the distance between the centre points of the two objects.
     * If the combined radii of the two objects is grater
     * than the distance between the two object centers, we have an overlap.
     *
     * @param cp1
     * @param cp2
     * @return True if the radii overlap.
     */
    public static boolean detect(CollisionPackage cp1, CollisionPackage cp2) {
        boolean collided = false;

        // Check circle collision between the two objects.

        // Get the distance of the two objects from the centre of the circles on the x axis.
        float distanceX = (cp1.worldLocation.x) - (cp2.worldLocation.x);
        // Get the distance of the two objects from the centre of the circles on the y axis.
        float distanceY = (cp1.worldLocation.y) - (cp2.worldLocation.y);
        // Calculate the distance between the center of each circle.
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        // Finally see if the two circles overlap
        // If they do it is worth doing the more intensive and accurate check.
        if (distance < cp1.radius + cp2.radius) {
            // Log.e("Circle collision:", "true");
            collided = true;
        }
        return collided;
    }

    /**
     * Checks if anything hits the border.
     * <p>
     * Carries out a simple intersect test and return true if detected.
     *
     * @param mapWidth
     * @param mapHeight
     * @param cp
     * @return True if an intersection is detected.
     */
    public static boolean contain(float mapWidth, float mapHeight, CollisionPackage cp) {
        boolean possibleCollision = false;

        // Check if any corner of a virtual rectangle around the centre of the object is out of bounds.
        // Rectangle is the best because we are testing against straight sides (the border).
        // If it is we have a possible collision.
        if (cp.worldLocation.x - cp.radius < 0) {
            possibleCollision = true;
        } else if (cp.worldLocation.x + cp.radius > mapWidth) {
            possibleCollision = true;
        } else if (cp.worldLocation.y - cp.radius < 0) {
            possibleCollision = true;
        } else if (cp.worldLocation.y + cp.radius > mapHeight) {
            possibleCollision = true;
        }

        if (possibleCollision) {
            double radianAngle = ((cp.facingAngle / 180) * Math.PI);
            double cosAngle = Math.cos(radianAngle);
            double sinAngle = Math.sin(radianAngle);

            // Rotate each and every vertex then check for a collision.
            // If just one is colliding then we have a collision.
            // Once we have a collision no need to check further.
            for (int i = 0; i < cp.vertexListLength; i++) {
                // First update the regular un-rotated model space coordinates
                // relative to the current world location (centre of object).
                float worldUnrotatedX = cp.worldLocation.x + cp.vertexList[i].x;
                float worldUnrotatedY = cp.worldLocation.y + cp.vertexList[i].y;
                // Now rotate the newly updated point, stored in currentPoint
                // around the centre point of the object (worldLocation).
                cp.currentPoint.x = cp.worldLocation.x + (int) ((worldUnrotatedX - cp.worldLocation.x)
                        * cosAngle - (worldUnrotatedY - cp.worldLocation.y) * sinAngle);
                cp.currentPoint.y = cp.worldLocation.y + (int) ((worldUnrotatedX - cp.worldLocation.x)
                        * sinAngle + (worldUnrotatedY - cp.worldLocation.y) * cosAngle);

                // Check the rotated vertex for a collision.
                if (cp.currentPoint.x < 0) {
                    return true;
                } else if (cp.currentPoint.x > mapWidth) {
                    return true;
                } else if (cp.currentPoint.y < 0) {
                    return true;
                } else if (cp.currentPoint.y > mapHeight) {
                    return true;
                }
            }
        }
        return false;
    }
}
