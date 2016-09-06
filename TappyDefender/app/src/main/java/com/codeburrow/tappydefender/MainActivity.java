package com.codeburrow.tappydefender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final boolean DEBUGGING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prepare to load fastest time.
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        final Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        // Get a refference to the TextView in our layout
        final TextView textFastestTime = (TextView) findViewById(R.id.textHiScore);

        // Listen for clicks.
        buttonPlay.setOnClickListener(this);

        // Load fastest time, if not available our high score = 1000000.
        long fastestTime = prefs.getLong("fastestTime", 1000000);
        // Put the high score in our TextView.
        textFastestTime.setText("Fastest Time:" + fastestTime);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        // Start the GameActivity class via the Intent.
        startActivity(intent);
        // And shut this activity down.
        finish();
    }
}
