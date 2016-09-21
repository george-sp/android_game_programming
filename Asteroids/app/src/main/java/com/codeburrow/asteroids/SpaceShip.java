package com.codeburrow.asteroids;

/**
 * Space ship is the player's game object.
 */
public class SpaceShip extends GameObject {

    boolean isThrusting;
    private boolean isPressingRight = false;
    private boolean isPressingLeft = false;

    public SpaceShip(float worldLocationX, float worldLocationY) {
        super();

        // Make sure we know this object is a ship.
        // So the draw() method knows what type of primitive to construct from the vertices.
        setType(Type.SHIP);

        setWorldLocation(worldLocationX, worldLocationY);

        float width = 15;
        float length = 20;

        setSize(width, length);
        setMaxSpeed(150);

        // It will be useful to have a copy of the length and width/2
        // so we don't have to keep dividing by 2.
        float halfW = width / 2;
        float halfL = length / 2;

        // Define the space ship shape as a triangle from point to point
        // in anti clockwise order.
        float[] shipVertices = new float[]{
                -halfW, -halfL, 0,
                halfW, -halfL, 0,
                0, 0 + halfL, 0
        };
        setVertices(shipVertices);
    }

    public void setPressingRight(boolean pressingRight) {
        this.isPressingRight = pressingRight;
    }

    public void setPressingLeft(boolean pressingLeft) {
        this.isPressingLeft = pressingLeft;
    }

    public void toggleThrust() {
        isThrusting = !isThrusting;
    }

    public void update(long fps) {
        float speed = getSpeed();

        // Adjust the speed based on whether isThrusting is true or false.
        if (isThrusting) {
            if (speed < getMaxSpeed()) {
                setSpeed(speed + 5);
            }
        } else {
            if (speed > 0) {
                setSpeed(speed - 3);
            } else {
                setSpeed(0);
            }
        }

        /*
         * Explanations here:
         * - http://stackoverflow.com/questions/14086721/finding-velocity-vector-based-on-angle-and-speed
         * - http://stackoverflow.com/questions/9881275/problems-with-cos-and-sin-in-android
         */
        // Set the x and y velocity based on the angle.
        setxVelocity((float) (speed * Math.cos(Math.toRadians(getFacingAngle() + 90))));
        setyVelocity((float) (speed * Math.sin(Math.toRadians(getFacingAngle() + 90))));

        // Set the rotationRate based on whether the player is turning left or right.
        if (isPressingLeft) {
            setRotationRate(360);
        } else if (isPressingRight) {
            setRotationRate(-360);
        } else {
            setRotationRate(0);
        }

        move(fps);
    }
}
