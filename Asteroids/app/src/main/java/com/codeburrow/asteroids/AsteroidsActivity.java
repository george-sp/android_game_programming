package com.codeburrow.asteroids;

import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

public class AsteroidsActivity extends AppCompatActivity {

    // This is the class that will provide us with easy access to OpenGL.
    private GLSurfaceView asteroidsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details.
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object.
        Point resolution = new Point();
        display.getSize(resolution);
        asteroidsView = new AsteroidsView(this, resolution.x, resolution.y);
        setContentView(asteroidsView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        asteroidsView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        asteroidsView.onResume();
    }
}
