package com.codeburrow.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Manages the game level.
 */
public class LevelManager {

    // The name of the level
    private String level;
    // Width and height in game world meters of the current level
    int mapWidth;
    int mapHeight;

    // The player object
    Player player;
    int playerIndex;

    private boolean playing;
    float gravity;

    LevelData levelData;
    // All the game objects
    ArrayList<GameObject> gameObjects;
    // Representations of the player control buttons
    ArrayList<Rect> currentButtons;
    // All the needed bitmaps
    Bitmap[] bitmapsArray;

    public LevelManager(Context context, int pixelsPerMetre, int screenWidth, InputController ic, String level, float px, float py) {
        this.level = level;

        switch (level) {
            case "LevelCave":
                levelData = new LevelCave();
                break;
        }

        gameObjects = new ArrayList<>();

        bitmapsArray = new Bitmap[25];

        // Load all the GameObjects and Bitmaps.
        loadMapData(context, pixelsPerMetre, px, py);
    }

    /**
     * Switch the playing status between playing and not playing.
     */
    public void switchPlayingStatus() {
        playing = !playing;
        if (playing) {
            gravity = 6;
        } else {
            gravity = 0;
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    /**
     * Returns a bitmap based on the inserted blockType.
     *
     * @param blockType The block type of the tile represented by a char
     * @return
     */
    public Bitmap getBitmap(char blockType) {
        // Each index Corresponds to a bitmap.
        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            case 'c':
                index = 3;
                break;
            case 'u':
                index = 4;
                break;
            case 'e':
                index = 5;
                break;
            case 'd':
                index = 6;
                break;
            default:
                index = 0;
                break;
        }
        return bitmapsArray[index];
    }

    /**
     * Returns an index that matches the index of the appropriate Bitmap
     * held in the bitmapsArray.
     * <p/>
     * Only one copy of each Bitmap object is needed.
     *
     * @param blockType The block type of the tile represented by a char
     * @return An index of a Bitmap in the bitmapsArray
     */
    public int getBitmapIndex(char blockType) {
        int index;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            case 'c':
                index = 3;
                break;
            case 'u':
                index = 4;
                break;
            case 'e':
                index = 5;
                break;
            case 'd':
                index = 6;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }

    /**
     * For now we just load all the grass tiles and the player.
     *
     * @param context
     * @param pixelsPerMetre
     * @param px
     * @param py
     */
    void loadMapData(Context context, int pixelsPerMetre, float px, float py) {
        char c;

        // Keep track of where we load our game objects.
        int currentIndex = -1;
        // How wide and high is the map.
        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j < levelData.tiles.get(i).length(); j++) {
                c = levelData.tiles.get(i).charAt(j);
                // Check if there is an empty space.
                if (c != '.') {
                    currentIndex++;
                    switch (c) {
                        case '1':
                            // Add a tile to the gameObjects.
                            gameObjects.add(new Grass(j, i, c));
                            break;
                        case 'p':// a player
                            // Add a player to the gameObjects.
                            gameObjects.add(new Player
                                    (context, px, py, pixelsPerMetre));

                            playerIndex = currentIndex;
                            // We want a reference to the player object
                            player = (Player) gameObjects.get(playerIndex);
                            break;
                        case 'c':
                            // Add a coin to the gameObjects.
                            gameObjects.add(new Coin(j, i, c));
                            break;
                        case 'u':
                            // Add a machine gun upgrade to the gameObjects.
                            gameObjects.add(new MachineGunUpgrade(j, i, c));
                            break;
                        case 'e':
                            // Add an extra life to the gameObjects.
                            gameObjects.add(new ExtraLife(j, i, c));
                            break;
                        case 'd':
                            // Add a drone to the gameObjects.
                            gameObjects.add(new Drone(j, i, c));
                            break;
                    }
                }

                if (bitmapsArray[getBitmapIndex(c)] == null) {
                    bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(), pixelsPerMetre);
                }
            }
        }
    }
}
