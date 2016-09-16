package com.codeburrow.platformer.game_engine;

import android.graphics.Rect;

/**
 * It defines the area of the game world to be shown to the player.
 * Typically, it will center on Player object.
 */
public class Viewport {

    // Represents whatever point in the world is currently the central focus in the viewport.
    private Vector2Point currentViewportWorldCentre;
    private Rect convertedRect;
    private int pixelsPerMetreX;
    private int pixelsPerMetreY;
    // The resolution of the screen in both axes.
    private int screenXResolution;
    private int screenYResolution;
    // The previous variables divided by two, to find the middle.
    private int screenCentreX;
    private int screenCentreY;
    // The number of meters to be squashed into the viewport.
    private int metresToShowX;
    private int metresToShowY;
    private int numClipped;

    Viewport(int x, int y) {
        screenXResolution = x;
        screenYResolution = y;

        screenCentreX = screenXResolution / 2;
        screenCentreY = screenYResolution / 2;

        pixelsPerMetreX = screenXResolution / 32;
        pixelsPerMetreY = screenYResolution / 18;

        metresToShowX = 34;
        metresToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCentre = new Vector2Point();
    }

    void setWorldCentre(float x, float y) {
        currentViewportWorldCentre.x = x;
        currentViewportWorldCentre.y = y;

    }

    public int getScreenWidth() {
        return screenXResolution;
    }

    public int getScreenHeight() {
        return screenYResolution;
    }

    public int getPixelsPerMetreX() {
        return pixelsPerMetreX;
    }

    public int getPixelsPerMetreY() {
        return pixelsPerMetreY;
    }

    public int getyCentre() {
        return screenCentreY;
    }

    public float getViewportWorldCentreY() {
        return currentViewportWorldCentre.y;
    }

    /**
     * Converts the locations of all the objects currently in the visible viewport
     * from world coordinates to pixel coordinates that can actually be drawn to the screen.
     *
     * @param objectX      The x coordinate of the object's location.
     * @param objectY      The y coordinate of the object's location.
     * @param objectWidth  The object's width.
     * @param objectHeight The object's height.
     * @return
     */
    public Rect worldToScreen(float objectX, float objectY, float objectWidth, float objectHeight) {
        int left = (int) (screenCentreX - ((currentViewportWorldCentre.x - objectX) * pixelsPerMetreX));
        int top = (int) (screenCentreY - ((currentViewportWorldCentre.y - objectY) * pixelsPerMetreY));
        int right = (int) (left + (objectWidth * pixelsPerMetreX));
        int bottom = (int) (top + (objectHeight * pixelsPerMetreY));
        convertedRect.set(left, top, right, bottom);
        return convertedRect;
    }

    /**
     * Remove objects that are currently of no interest to user.
     *
     * @param objectX      The x coordinate of the object's location.
     * @param objectY      The y coordinate of the object's location.
     * @param objectWidth  The object's width.
     * @param objectHeight The object's height.
     * @return Rect object. A ready to be drawn rectangle.
     */
    public boolean clipObjects(float objectX, float objectY, float objectWidth, float objectHeight) {
        /*
         * The check starts by assuming that the current object needs to be clipped
         * and so clipped is assigned to true.
         */
        boolean clipped = true;

        /*
         * The four nested if statements check
         * whether each and every point of the object is within the bounds
         * of the related side of the viewport.
         */
        if (objectX - objectWidth < currentViewportWorldCentre.x + (metresToShowX / 2)) {
            if (objectX + objectWidth > currentViewportWorldCentre.x - (metresToShowX / 2)) {
                if (objectY - objectHeight < currentViewportWorldCentre.y + (metresToShowY / 2)) {
                    if (objectY + objectHeight > currentViewportWorldCentre.y - (metresToShowY / 2)) {
                        /*
                         * If it is, we set clipped to false.
                         */
                        clipped = false;
                    }
                }
            }
        }

        // For debugging
        if (clipped) {
            numClipped++;
        }

        return clipped;
    }

    public int getNumClipped() {
        return numClipped;
    }

    public void resetNumClipped() {
        numClipped = 0;
    }
}
