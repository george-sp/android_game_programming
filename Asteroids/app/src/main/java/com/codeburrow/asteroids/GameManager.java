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
    Border border;
    Star[] stars;
    int numStars = 200;
    Bullet[] bullets;
    int numBullets = 20;
    Asteroid[] asteroids;
    int numAsteroids;
    int numAsteroidsRemaining;
    int baseNumAsteroids = 10;
    int levelNumber = 1;
    TallyIcon[] tallyIcons;
    int numLives = 3;
    LifeIcon[] lifeIcons;

    int screenWidth;
    int screenHeight;

    // How many metres of our virtual world  will be shown on screen at any time.
    int metresToShowX = 390;
    int metresToShowY = 220;

    public GameManager(int x, int y) {
        screenWidth = x;
        screenHeight = y;

        // For all our asteroids.
        asteroids = new Asteroid[500];

        tallyIcons = new TallyIcon[500];
        lifeIcons = new LifeIcon[50];
    }

    public void switchPlayingStatus() {
        playing = !playing;
    }

    public boolean isPlaying() {
        return playing;
    }
}
