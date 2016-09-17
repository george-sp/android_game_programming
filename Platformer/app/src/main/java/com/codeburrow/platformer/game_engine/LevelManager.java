package com.codeburrow.platformer.game_engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.codeburrow.platformer.background.Background;
import com.codeburrow.platformer.background.BackgroundData;
import com.codeburrow.platformer.enemy.Drone;
import com.codeburrow.platformer.enemy.Guard;
import com.codeburrow.platformer.levels.LevelCave;
import com.codeburrow.platformer.levels.LevelCity;
import com.codeburrow.platformer.levels.LevelForest;
import com.codeburrow.platformer.levels.LevelMountain;
import com.codeburrow.platformer.pickups.Coin;
import com.codeburrow.platformer.pickups.ExtraLife;
import com.codeburrow.platformer.pickups.MachineGunUpgrade;
import com.codeburrow.platformer.platform_tiles.Brick;
import com.codeburrow.platformer.platform_tiles.Coal;
import com.codeburrow.platformer.platform_tiles.Concrete;
import com.codeburrow.platformer.platform_tiles.Fire;
import com.codeburrow.platformer.platform_tiles.Grass;
import com.codeburrow.platformer.platform_tiles.Scorched;
import com.codeburrow.platformer.platform_tiles.Snow;
import com.codeburrow.platformer.platform_tiles.Stone;
import com.codeburrow.platformer.player.Player;
import com.codeburrow.platformer.scenery_objects.Boulders;
import com.codeburrow.platformer.scenery_objects.Cart;
import com.codeburrow.platformer.scenery_objects.Lampost;
import com.codeburrow.platformer.scenery_objects.Stalactite;
import com.codeburrow.platformer.scenery_objects.Stalagmite;
import com.codeburrow.platformer.scenery_objects.Tree;
import com.codeburrow.platformer.scenery_objects.Tree2;
import com.codeburrow.platformer.special_objects.Teleport;

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
    ArrayList<Background> backgrounds;
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
            case "LevelCity":
                levelData = new LevelCity();
                break;
            case "LevelForest":
                levelData = new LevelForest();
                break;
            case "LevelMountain":
                levelData = new LevelMountain();
                break;
        }

        gameObjects = new ArrayList<>();

        bitmapsArray = new Bitmap[25];

        // Load all the GameObjects and Bitmaps.
        loadMapData(context, pixelsPerMetre, px, py);
        loadBackgrounds(context, pixelsPerMetre, screenWidth);

        // Set waypoints for our guards.
        setWaypoints();
    }

    private void loadBackgrounds(Context context, int pixelsPerMetre, int screenWidth) {
        backgrounds = new ArrayList<>();
        // Load the background data into the Background objects and
        // place them in our GameObject array list.
        for (BackgroundData bgData : levelData.backgroundDataList) {
            backgrounds.add(new Background(context, pixelsPerMetre, screenWidth, bgData));
        }
    }

    public void setWaypoints() {
        // Loop through all game objects looking for Guards.
        for (GameObject guard : this.gameObjects) {
            if (guard.getType() == 'g') {
                /*
                 * Set the waypoints for this guard.
                 * Find the tile which the guard is standing on.
                 * Calculate the coordinates of the last traversable tile on either side
                 * but with a maximum range of five tiles each way.
                 * These will be the two waypoints.
                 */
                int startTileIndex = -1;
                int startGuardIndex = 0;
                float waypointX1 = -1;
                float waypointX2 = -1;

                for (GameObject tile : this.gameObjects) {
                    startTileIndex++;
                    if (tile.getWorldLocation().y == guard.getWorldLocation().y + 2) {
                        // Tile is two spaces below current guard.
                        if (tile.getWorldLocation().x == guard.getWorldLocation().x) {
                            // The loop for the left waypoint.
                            for (int i = 0; i < 5; i++) {
                                if (!gameObjects.get(startTileIndex - i).isTraversable()) {
                                    //set the left waypoint
                                    waypointX1 = gameObjects.get(startTileIndex - (i + 1)).getWorldLocation().x;
                                    break;// Leave left for loop
                                } else {
                                    //set to max 5 tiles as no non traversible tile found
                                    waypointX1 = gameObjects.get(startTileIndex - 5).getWorldLocation().x;
                                }
                            }
                            // The loop for the right waypoint.
                            for (int i = 0; i < 5; i++) {
                                if (!gameObjects.get(startTileIndex + i).isTraversable()) {
                                    //set the right waypoint
                                    waypointX2 = gameObjects.get(startTileIndex + (i - 1)).getWorldLocation().x;
                                    break;// Leave right for loop
                                } else {
                                    //set to max 5 tiles away
                                    waypointX2 = gameObjects.get(startTileIndex + 5).getWorldLocation().x;
                                }
                            }

                            Guard g = (Guard) guard;
                            g.setWaypoints(waypointX1, waypointX2);
                        }
                    }
                }
            }
        }
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
            case 'g':
                index = 7;
                break;
            case 'f':
                index = 8;
                break;
            case '2':
                index = 9;
                break;
            case '3':
                index = 10;
                break;
            case '4':
                index = 11;
                break;
            case '5':
                index = 12;
                break;
            case '6':
                index = 13;
                break;
            case '7':
                index = 14;
                break;
            case 'w':
                index = 15;
                break;
            case 'x':
                index = 16;
                break;
            case 'l':
                index = 17;
                break;
            case 'r':
                index = 18;
                break;
            case 's':
                index = 19;
                break;
            case 'm':
                index = 20;
                break;
            case 'z':
                index = 21;
                break;
            case 't':
                index = 22;
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
            case 'g':
                index = 7;
                break;
            case '2':
                index = 9;
                break;
            case '3':
                index = 10;
                break;
            case '4':
                index = 11;
                break;
            case '5':
                index = 12;
                break;
            case '6':
                index = 13;
                break;
            case '7':
                index = 14;
                break;
            case 'f':
                index = 8;
                break;
            case 'w':
                index = 15;
                break;
            case 'x':
                index = 16;
                break;
            case 'l':
                index = 17;
                break;
            case 'r':
                index = 18;
                break;
            case 's':
                index = 19;
                break;
            case 'm':
                index = 20;
                break;
            case 'z':
                index = 21;
                break;
            case 't':
                index = 22;
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
        int teleportIndex = -1;
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
                            gameObjects.add(new Player(context, px, py, pixelsPerMetre));
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
                        case 'g':
                            // Add a guard to the gameObjects.
                            gameObjects.add(new Guard(context, j, i, c, pixelsPerMetre));
                            break;
                        case 'f':
                            // Add a fire tile the gameObjects.
                            gameObjects.add(new Fire(context, j, i, c, pixelsPerMetre));
                            break;
                        case '2':
                            // Add a tile to the gameObjects.
                            gameObjects.add(new Snow(j, i, c));
                            break;
                        case '3':
                            // Add a tile to the gameObjects.
                            gameObjects.add(new Brick(j, i, c));
                            break;
                        case '4':
                            // Add a tile to the gameObjects.
                            gameObjects.add(new Coal(j, i, c));
                            break;
                        case '5':
                            // Add a tile to the gameObjects.
                            gameObjects.add(new Concrete(j, i, c));
                            break;
                        case '6':
                            // Add a tile to the gameObjects.
                            gameObjects.add(new Scorched(j, i, c));
                            break;
                        case '7':
                            // Add a tile to the gameObjects.
                            gameObjects.add(new Stone(j, i, c));
                            break;
                        case 'w':
                            // Add a tree to the gameObjects
                            gameObjects.add(new Tree(j, i, c));
                            break;
                        case 'x':
                            // Add a tree2 to the gameObjects
                            gameObjects.add(new Tree2(j, i, c));
                            break;
                        case 'l':
                            // Add a tree to the gameObjects
                            gameObjects.add(new Lampost(j, i, c));
                            break;
                        case 'r':
                            // Add a stalactite to the gameObjects
                            gameObjects.add(new Stalactite(j, i, c));
                            break;
                        case 's':
                            // Add a stalagmite to the gameObjects
                            gameObjects.add(new Stalagmite(j, i, c));
                            break;
                        case 'm':
                            // Add a cart to the gameObjects
                            gameObjects.add(new Cart(j, i, c));
                            break;
                        case 'z':
                            // Add a boulders to the gameObjects
                            gameObjects.add(new Boulders(j, i, c));
                            break;
                        case 't':
                            // Add a teleport to the gameObjects.
                            teleportIndex++;
                            gameObjects.add(new Teleport(j, i, c, levelData.locations.get(teleportIndex)));
                            break;
                    }
                    if (bitmapsArray[getBitmapIndex(c)] == null) {
                        bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(), pixelsPerMetre);
                    }
                }
            }
        }
    }
}
