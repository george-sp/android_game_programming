package com.codeburrow.platformer;

/**
 * The first game level.
 * <p>
 * Enter a different alpha-numeric character depending
 * on which GameObject we want to place into the level.
 */
public class LevelCave extends LevelData {

    public LevelCave() {
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("...............p.....111111...................");
        this.tiles.add("..............................................");
        this.tiles.add("............111111............................");
        this.tiles.add("..............................................");
        this.tiles.add(".........1111111..............................");
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("..............................................");
        this.tiles.add("..............................11111111........");
        this.tiles.add("..............................................");
    }
}
