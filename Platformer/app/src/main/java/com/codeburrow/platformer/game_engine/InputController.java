package com.codeburrow.platformer.game_engine;

import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Detects the player's input.
 */
public class InputController {

    Rect left;
    Rect right;
    Rect jump;
    Rect shoot;
    Rect pause;

    InputController(int screenWidth, int screenHeight) {
        // Configure the player buttons.
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        left = new Rect(buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth,
                screenHeight - buttonPadding);

        right = new Rect(buttonWidth + buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth + buttonPadding + buttonWidth,
                screenHeight - buttonPadding);

        jump = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding - buttonHeight - buttonPadding);

        shoot = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding);

        pause = new Rect(screenWidth - buttonPadding - buttonWidth,
                buttonPadding,
                screenWidth - buttonPadding,
                buttonPadding + buttonHeight);
    }

    public ArrayList getButtons() {
        // Create an array of buttons for the draw method.
        ArrayList<Rect> currentButtonList = new ArrayList<>();
        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(jump);
        currentButtonList.add(shoot);
        currentButtonList.add(pause);
        return currentButtonList;
    }

    public void handleInput(MotionEvent motionEvent, LevelManager levelManager, SoundManager sound, Viewport viewport) {
        int pointerCount = motionEvent.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            if (levelManager.isPlaying()) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        if (right.contains(x, y)) {
                            levelManager.player.setPressingRight(true);
                            levelManager.player.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            levelManager.player.setPressingLeft(true);
                            levelManager.player.setPressingRight(false);
                        } else if (jump.contains(x, y)) {
                            levelManager.player.startJump(sound);
                        } else if (shoot.contains(x, y)) {
                            if (levelManager.player.pullTrigger()) {
                                sound.playSound("shoot");
                            }
                        } else if (pause.contains(x, y)) {
                            levelManager.switchPlayingStatus();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (right.contains(x, y)) {
                            levelManager.player.setPressingRight(false);
                        } else if (left.contains(x, y)) {
                            levelManager.player.setPressingLeft(false);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (right.contains(x, y)) {
                            levelManager.player.setPressingRight(true);
                            levelManager.player.setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            levelManager.player.setPressingLeft(true);
                            levelManager.player.setPressingRight(false);
                        } else if (jump.contains(x, y)) {
                            levelManager.player.startJump(sound);
                        } else if (shoot.contains(x, y)) {
                            if (levelManager.player.pullTrigger()) {
                                sound.playSound("shoot");
                            }
                        } else if (pause.contains(x, y)) {
                            levelManager.switchPlayingStatus();
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        if (right.contains(x, y)) {
                            levelManager.player.setPressingRight(false);
                        } else if (left.contains(x, y)) {
                            levelManager.player.setPressingLeft(false);
                        } else if (shoot.contains(x, y)) {
                            // Handle shooting here
                        } else if (jump.contains(x, y)) {
                            // Handle more jumping stuff here later
                        }
                        break;
                }
            } else {
                // Not playing.
                // Move the viewport around to explore the map.
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (right.contains(x, y)) {
                            viewport.moveViewportRight(levelManager.mapWidth);
                        } else if (left.contains(x, y)) {
                            viewport.moveViewportLeft();
                        } else if (jump.contains(x, y)) {
                            viewport.moveViewportUp();
                        } else if (shoot.contains(x, y)) {
                            viewport.moveViewportDown(levelManager.mapHeight);
                        } else if (pause.contains(x, y)) {
                            levelManager.switchPlayingStatus();
                        }
                        break;
                }
            }
        }
    }

}
