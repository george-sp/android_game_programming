package com.codeburrow.asteroids;

import android.graphics.PointF;

import java.util.Random;

public class Asteroid extends GameObject {

    PointF[] points;

    public Asteroid(int levelNumber, int mapWidth, int mapHeight) {
        super();

        // Set a random rotation rate in degrees per second.
        Random r = new Random();
        setRotationRate(r.nextInt(50 * levelNumber) + 10);

        // Travel at any random angle.
        setTravellingAngle(r.nextInt(360));

        // Spawn asteroids between 50 and 550 on x and y, and avoid the extreme edges of map.
        int x = r.nextInt(mapWidth - 100) + 50;
        int y = r.nextInt(mapHeight - 100) + 50;

        // Avoid the center where the player spawns.
        if (x > 250 && x < 350) x = x + 100;
        if (y > 250 && y < 350) y = y + 100;

        // Set the location.
        setWorldLocation(x, y);

        // Make them a random speed with the maximum being appropriate to the level number.
        setSpeed(r.nextInt(25 * levelNumber) + 1);

        setMaxSpeed(140);

        // Cap the speed.
        if (getSpeed() > getMaxSpeed()) {
            setSpeed(getMaxSpeed());
        }

        // Make sure we know this object is a ship.
        setType(Type.ASTEROID);

        // Define a random asteroid shape.
        // Then call the parent setVertices().
        generatePoints();
    }

    /**
     * Calculates the velocity based on speed and travelling angle.
     *
     * @param fps
     */
    public void update(float fps) {
        setxVelocity((float) (getSpeed() * Math.cos(Math.toRadians(getTravellingAngle() + 90))));
        setyVelocity((float) (getSpeed() * Math.sin(Math.toRadians(getTravellingAngle() + 90))));

        move(fps);
    }
}
