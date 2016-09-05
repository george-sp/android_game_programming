package com.codeburrow.tappydefender;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

public class GameActivity extends AppCompatActivity {

    // The object to handle the game view.
    private TappyDefenderView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details.
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object.
        Point point = new Point();
        display.getSize(point);

        // Create an instance of our TappyDefenderView.
        gameView = new TappyDefenderView(this, point.x, point.y);
        // Make our "gameView" the view for the Activity.
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // If the Activity is paused make sure to pause our thread.
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If the Activity is resumed make sure to resume our thread.
        gameView.resume();
    }
}
