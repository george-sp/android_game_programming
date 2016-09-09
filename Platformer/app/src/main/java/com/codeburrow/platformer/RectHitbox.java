package com.codeburrow.platformer;

/**
 * It is provided to every game object so that the Player class
 * can check if the hit-box has been collided with the player.
 * <p/>
 * Once per frame, all hit-boxes that have not been clipped by the viewport
 * will be sent to Player class where a collision can be checked.
 */
public class RectHitbox {

    float top;
    float left;
    float bottom;
    float right;
    float height;

    boolean intersects(RectHitbox rectHitbox) {
        boolean hit = false;

        if (this.right > rectHitbox.left && this.left < rectHitbox.right) {
            // Intersecting on x axis.
            if (this.top < rectHitbox.bottom && this.bottom > rectHitbox.top) {
                // Intersecting on y as well.
                // There is a collision.
                hit = true;
            }
        }
        return hit;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
