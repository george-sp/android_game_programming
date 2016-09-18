package com.codeburrow.asteroids;

/**
 * This class control things like:
 * - the level the player is on
 * - the number of lives
 * - the overall size of the game world
 */
public class GameManager {

    int mapWidth = 600;
    int mapHeight = 600;
    private boolean playing = false;

    SpaceShip ship;

    int screenWidth;
    int screenHeight;

    // How many metres of our virtual world  will be shown on screen at any time.
    int metresToShowX = 390;
    int metresToShowY = 220;

    public GameManager(int x, int y) {
        screenWidth = x;
        screenHeight = y;
    }

    public void switchPlayingStatus() {
        playing = !playing;
    }

    public boolean isPlaying() {
        return playing;
    }
}
