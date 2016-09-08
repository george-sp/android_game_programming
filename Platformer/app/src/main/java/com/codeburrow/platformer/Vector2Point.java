package com.codeburrow.platformer;

/**
 * Represents the world location of all GameObject objects.
 */
public class Vector2Point {

    /*
     * Hold the detailed location on both the x and y axis.
     * These (x,y coordinates) are totally independent to
     * the coordinates of the pixels of the device on which our game will run.
     */
    float x;
    float y;
    // Z coordinate: layer number - The lower number get drawn first.
    int z;
}
