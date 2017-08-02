package com.gumanx.dotdodger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static int dotColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loads stored data
        SharedPreferences prefs = getSharedPreferences("dotDodgePrefs", Context.MODE_PRIVATE);
        dotColor = prefs.getInt("dotColor", Color.CYAN);

        // Sets high score
        TextView scoreIntText = (TextView) findViewById(R.id.scoreIntText);
        scoreIntText.setText(Integer.toString(prefs.getInt("highScore", 0)));
    }

    public void startGameActivity(View view) {
        setContentView(new GameView(this));
    }

    public void startCustomizeActivity(View view) {
        Intent intent = new Intent(this, CustomizeActivity.class);
        startActivity(intent);
    }
}
