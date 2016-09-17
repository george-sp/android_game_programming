package com.codeburrow.platformer.game_engine;

import com.codeburrow.platformer.background.BackgroundData;
import com.codeburrow.platformer.special_objects.Location;

import java.util.ArrayList;

/**
 * Holds the data for the game level.
 * <p/>
 * (Extend it each time a new level design is created.)
 */
public class LevelData {

    public ArrayList<String> tiles;
    public ArrayList<BackgroundData> backgroundDataList;
    public ArrayList<Location> locations;

    /*
     * Tile types
     * ------------------------
     * . = no tile
     * 1 = Grass
     * 2 = Snow
     * 3 = Brick
     * 4 = Coal
     * 5 = Concrete
     * 6 = Scorched
     * 7 = Stone
     */

    /*
     * Active objects
     * g = guard
     * d = drone
     * t = teleport
     * c = coin
     * u = upgrade
     * f = fire
     * e  = extra life
     */

    /*
     * Inactive objects
     * w = tree
     * x = tree2 (snowy)
     * l = lampost
     * r = stalactite
     * s = stalacmite
     * m = mine cart
     * z = boulders
     */
}
