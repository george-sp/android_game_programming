package com.codeburrow.asteroids;

import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class InputController {

    // Keep track of which buullet is going to be shot next.
    private int currentBullet;

    Rect left;
    Rect right;
    Rect thrust;
    Rect shoot;
    Rect pause;

    InputController(int screenWidth, int screenHeight) {
        // Configure the player buttons.
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        left = new Rect(
                buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth,
                screenHeight - buttonPadding);

        right = new Rect(
                buttonWidth + buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth + buttonPadding + buttonWidth,
                screenHeight - buttonPadding);

        thrust = new Rect(
                screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding - buttonHeight - buttonPadding);

        shoot = new Rect(
                screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding);

        pause = new Rect(
                screenWidth - buttonPadding - buttonWidth,
                buttonPadding,
                screenWidth - buttonPadding,
                buttonPadding + buttonHeight);
    }

    /**
     * Bundles the buttons together in a list.
     *
     * @return An ArrayList of Buttons.
     */
    public ArrayList getButtons() {
        // Create an array of buttons for the draw method.
        ArrayList<Rect> currentButtonList = new ArrayList<>();
        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(thrust);
        currentButtonList.add(shoot);
        currentButtonList.add(pause);
        return currentButtonList;
    }

    public void handleInput(MotionEvent motionEvent, GameManager gameManager, SoundManager sound) {
        int pointerCount = motionEvent.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (right.contains(x, y)) {
                        gameManager.ship.setPressingRight(true);
                        gameManager.ship.setPressingLeft(false);
                    } else if (left.contains(x, y)) {
                        gameManager.ship.setPressingLeft(true);
                        gameManager.ship.setPressingRight(false);
                    } else if (thrust.contains(x, y)) {
                        gameManager.ship.toggleThrust();
                    } else if (shoot.contains(x, y)) {
                        if (gameManager.ship.pullTrigger()) {
                            gameManager.bullets[currentBullet].shoot(gameManager.ship.getFacingAngle());
                            currentBullet++;
                            // If we are on the last bullet restart from the first one again.
                            if (currentBullet == gameManager.numBullets) {
                                currentBullet = 0;
                            }
                            sound.playSound("shoot");
                        }
                    } else if (pause.contains(x, y)) {
                        gameManager.switchPlayingStatus();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (right.contains(x, y)) {
                        gameManager.ship.setPressingRight(false);
                    } else if (left.contains(x, y)) {
                        gameManager.ship.setPressingLeft(false);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (right.contains(x, y)) {
                        gameManager.ship.setPressingRight(true);
                        gameManager.ship.setPressingLeft(false);
                    } else if (left.contains(x, y)) {
                        gameManager.ship.setPressingLeft(true);
                        gameManager.ship.setPressingRight(false);
                    } else if (thrust.contains(x, y)) {
                        gameManager.ship.toggleThrust();
                    } else if (shoot.contains(x, y)) {
                        if (gameManager.ship.pullTrigger()) {
                            gameManager.bullets[currentBullet].shoot(gameManager.ship.getFacingAngle());
                            currentBullet++;
                            // If we are on the last bullet restart from the first one again.
                            if (currentBullet == gameManager.numBullets) {
                                currentBullet = 0;
                            }
                            sound.playSound("shoot");
                        }
                    } else if (pause.contains(x, y)) {
                        gameManager.switchPlayingStatus();
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    if (right.contains(x, y)) {
                        gameManager.ship.setPressingRight(false);
                    } else if (left.contains(x, y)) {
                        gameManager.ship.setPressingLeft(false);
                    }
                    break;
            }
        }
    }
}
