package com.codeburrow.platformer.player;

import android.graphics.PointF;

/**
 * Holds the state of the state of the current player,
 * so that the money collected, power of the machine-gun and remaining lives can be monitored.
 */
public class PlayerState {

    private int numCredits;
    private int machineGunFireRate;
    private int lives;
    private float restartX;
    private float restartY;

    public PlayerState() {
        lives = 3;
        machineGunFireRate = 1;
        numCredits = 0;
    }

    /**
     * The new location that should be saved and used as the respawn location.
     *
     * @param location
     */
    public void saveLocation(PointF location) {
        restartX = location.x;
        restartY = location.y;
    }

    /**
     * Loads the last saved location.
     * It is called each time the player loses a life.
     *
     * @return
     */
    public PointF loadLocation() {
        return new PointF(restartX, restartY);
    }

    public int getLives() {
        return lives;
    }

    public int getFireRate() {
        return machineGunFireRate;
    }

    public void increaseFireRate() {
        machineGunFireRate += 2;
    }


    public void gotCredit() {
        numCredits++;
    }

    public int getCredits() {
        return numCredits;
    }

    public void loseLife() {
        lives--;
    }

    public void addLife() {
        lives++;
    }

    public void resetLives() {
        lives = 3;
    }

    public void resetCredits() {
        lives = 0;
    }
}
