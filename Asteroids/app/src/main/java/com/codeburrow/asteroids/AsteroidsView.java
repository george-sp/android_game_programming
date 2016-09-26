package com.codeburrow.asteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class AsteroidsView extends GLSurfaceView {

    GameManager gameManager;
    SoundManager soundManager;
    InputController inputController;

    public AsteroidsView(Context context, int screenX, int screenY) {
        super(context);

        soundManager = new SoundManager();
        soundManager.loadSound(context);
        inputController = new InputController(screenX, screenY);
        gameManager = new GameManager(screenX, screenY);

        // Which version of OpenGl we are using.
        setEGLContextClientVersion(2);

        // Attach our renderer to the GLSurfaceView.
        setRenderer(new AsteroidsRenderer(gameManager, soundManager, inputController));
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        inputController.handleInput(motionEvent, gameManager, soundManager);
        return true;
    }
}
