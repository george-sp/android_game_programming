package com.codeburrow.platformer;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import com.codeburrow.platformer.game_engine.PlatformView;

public class PlatformActivity extends AppCompatActivity {

    // An object to handle the View.
    private PlatformView platformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to to get the properties of the physical display
        // hardware that our game will be running on.
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object.
        Point resolution = new Point();
        display.getSize(resolution);

        // Instantiate a new PlatformView object.
        platformView = new PlatformView(this, resolution.x, resolution.y);

        // Make platformView the view for the Activity, in the usual way.
        setContentView(platformView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        platformView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        platformView.resume();
    }
}
