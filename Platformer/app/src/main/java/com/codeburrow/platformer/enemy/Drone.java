package com.codeburrow.platformer.enemy;

import android.graphics.PointF;

import com.codeburrow.platformer.game_engine.GameObject;
import com.codeburrow.platformer.game_engine.Vector2Point;

/**
 * A simple but evil enemy.
 * It detects the player when it is within the viewport and fly straight at him.
 * If the drone touches the player, death is immediate.
 */
public class Drone extends GameObject {

    long lastWaypointSetTime;
    PointF currentWaypoint;

    final float MAX_X_VELOCITY = 3;
    final float MAX_Y_VELOCITY = 3;

    public Drone(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("drone");
        setMoves(true);
        setActive(true);
        setVisible(true);
        currentWaypoint = new PointF();
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
        setFacing(RIGHT);
    }

    public void update(long fps, float gravity) {
        if (currentWaypoint.x > getWorldLocation().x) {
            setxVelocity(MAX_X_VELOCITY);
        } else if (currentWaypoint.x < getWorldLocation().x) {
            setxVelocity(-MAX_X_VELOCITY);
        } else {
            setxVelocity(0);
        }
        if (currentWaypoint.y >= getWorldLocation().y) {
            setyVelocity(MAX_Y_VELOCITY);
        } else if (currentWaypoint.y < getWorldLocation().y) {
            setyVelocity(-MAX_Y_VELOCITY);
        } else {
            setyVelocity(0);
        }
        move(fps);
        // Update the drone hit-box.
        setRectHitbox();
    }

    public void setWaypoint(Vector2Point playerLocation) {
        if (System.currentTimeMillis() > lastWaypointSetTime + 2000) {//Has 2 seconds passed
            lastWaypointSetTime = System.currentTimeMillis();
            currentWaypoint.x = playerLocation.x;
            currentWaypoint.y = playerLocation.y;
        }
    }
}
