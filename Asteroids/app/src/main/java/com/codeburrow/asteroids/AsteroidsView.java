package com.codeburrow.asteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Sep/17/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class AsteroidsView extends GLSurfaceView {

    GameManager gameManager;

    public AsteroidsView(Context context, int screenX, int screenY) {
        super(context);

        gameManager = new GameManager(screenX, screenY);

        // Which version of OpenGl we are using.
        setEGLContextClientVersion(2);

        // Attach our renderer to the GLSurfaceView.
        setRenderer(new AsteroidsRenderer(gameManager));
    }
}
