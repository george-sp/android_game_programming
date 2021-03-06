package com.codeburrow.platformer.special_objects;

/*
 * Holds data that each Teleport object will need.
 */
public class Location {

    public String level;
    public float x;
    public float y;

    public Location(String level, float x, float y) {
        this.level = level;
        this.x = x;
        this.y = y;
    }
}