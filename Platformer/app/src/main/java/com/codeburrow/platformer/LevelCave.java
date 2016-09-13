package com.codeburrow.platformer;

import java.util.ArrayList;

/**
 * The first game level.
 * <p/>
 * Enter a different alpha-numeric character depending
 * on which GameObject we want to place into the level.
 */
public class LevelCave extends LevelData {

    public LevelCave() {
        tiles = new ArrayList<>();
        this.tiles.add("p.............................................");
        this.tiles.add(".....................................d........");
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("....................c.........................");
        this.tiles.add("....................1........u.........d......");
        this.tiles.add(".................c..........u1................");
        this.tiles.add("......d..........1.........u1.........d.......");
        this.tiles.add("..............c...........u1..................");
        this.tiles.add(".d............1..........u1...................");
        this.tiles.add("......................e..1....e.....e.........");
        this.tiles.add("....11111111111111111111111111111111111111....");
    }
}
