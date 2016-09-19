package com.codeburrow.asteroids;

import android.graphics.PointF;

import java.nio.FloatBuffer;

/**
 *
 */
public class GameObject {

    boolean isActive;

    public enum Type {SHIP, ASTEROID, BORDER, BULLET, STAR}

    private Type type;

    private static int glProgram = -1;

    // How many vertices does it take to make this particular game object?
    private int numElements;
    private int numVertices;

    // To hold the coordinates of the vertices that define our GameObject model.
    private float[] modelVertices;

    // Which way is the object moving and how fast?
    private float xVelocity = 0f;
    private float yVelocity = 0f;
    private float speed = 0;
    private float maxSpeed = 200;

    // Where is the object centre in the game world?
    private PointF worldLocation = new PointF();

    // This will hold our vertex data that is passed into the openGL glProgram OPenGL likes FloatBuffer.
    private FloatBuffer vertices;

    // For translating each point from the model (ship, asteroid etc) to its game world coordinates.
    private final float[] modelMatrix = new float[16];

    // Some more matrices for Open GL transformations.
    float[] viewportModelMatrix = new float[16];
    float[] rotateViewportModelMatrix = new float[16];

    // Where is the GameObject facing?
    private float facingAngle = 90f;

    // How fast is it rotating?
    private float rotationRate = 0f;

    // Which direction is it heading?
    private float travellingAngle = 0f;

    // How long and wide is the GameObject?
    private float length;
    private float width;
}
