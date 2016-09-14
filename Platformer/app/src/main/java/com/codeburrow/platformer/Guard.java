package com.codeburrow.platformer;

import android.content.Context;

/**
 * An enemy that will walk continuously between two waypoints on the X axis.
 */
public class Guard extends GameObject {

    // Always on left
    private float waypointX1;
    // Always on right
    private float waypointX2;
    private int currentWaypoint;
    final float MAX_X_VELOCITY = 3;

    Guard(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 5;
        final String BITMAP_NAME = "guard";
        final float HEIGHT = 2f;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("guard");
        setMoves(true);
        setActive(true);
        setVisible(true);
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);
        setWorldLocation(worldStartX, worldStartY, 0);
        setxVelocity(-MAX_X_VELOCITY);
        currentWaypoint = 1;
    }

    public void setWaypoints(float x1, float x2) {
        waypointX1 = x1;
        waypointX2 = x2;
    }

    public void update(long fps, float gravity) {
        if (currentWaypoint == 1) {
            // Heading left.
            if (getWorldLocation().x <= waypointX1) {
                // Arrived at waypoint 1.
                currentWaypoint = 2;
                setxVelocity(MAX_X_VELOCITY);
                setFacing(RIGHT);
            }
        }
        if (currentWaypoint == 2) {
            if (getWorldLocation().x >= waypointX2) {
                // Arrived at waypoint 2.
                currentWaypoint = 1;
                setxVelocity(-MAX_X_VELOCITY);
                setFacing(LEFT);
            }
        }
        move(fps);
        // Update the guard's hit-box.
        setRectHitbox();
    }

}
