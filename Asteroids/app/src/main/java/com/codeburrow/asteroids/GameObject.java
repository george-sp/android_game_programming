package com.codeburrow.asteroids;

import android.graphics.PointF;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUseProgram;

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

    public GameObject() {
        // Only compile shaders once.
        if (glProgram == -1) {
            setGLProgram();

            // Tell OpenGl to use the glProgram.
            glUseProgram(glProgram);

            // Now we have a glProgram we need the locations of our three GLSL variables.
            // We will use these when we call draw on the object.
            uMatrixLocation = glGetUniformLocation(glProgram, U_MATRIX);
            aPositionLocation = glGetAttribLocation(glProgram, A_POSITION);
            uColorLocation = glGetUniformLocation(glProgram, U_COLOR);
        }

        // Set the object as active
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setGLProgram() {
        glProgram = GLManager.getGLProgram();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type t) {
        this.type = t;
    }

    public void setSize(float w, float l) {
        width = w;
        length = l;
    }

    public PointF getWorldLocation() {
        return worldLocation;
    }

    public void setWorldLocation(float x, float y) {
        this.worldLocation.x = x;
        this.worldLocation.y = y;
    }
}
