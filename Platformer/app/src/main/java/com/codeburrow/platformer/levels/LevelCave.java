package com.codeburrow.platformer.levels;

import com.codeburrow.platformer.game_engine.LevelData;

import java.util.ArrayList;

/**
 * The first game level.
 * <p/>
 * Enter a different alpha-numeric character depending
 * on which GameObject we want to place into the level.
 */
public class LevelCave extends LevelData {

    /*
     * The guards 'g' should be placed with one space above the platform
     * and five traversable tiles either side.
     */

    public LevelCave() {
        tiles = new ArrayList<>();
        this.tiles.add("p.............................................");
        this.tiles.add(".....................................d........");
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("....................c.........................");
        this.tiles.add("....................1........u.........d......");
        this.tiles.add(".................c..........u1................");
        this.tiles.add(".................1.........u1.........d.......");
        this.tiles.add("..............c...........u1..................");
        this.tiles.add("..............1....g.....u1...........g.......");
        this.tiles.add("......................e..1....e.....e.........");
        this.tiles.add("ffff1111111111111111111111111111111111111111..");
    }
}