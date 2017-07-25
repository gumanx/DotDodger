package com.gumanx.dotdodger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    static int dotColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));

        // Loads stored data
        SharedPreferences prefs = getSharedPreferences("dotDodgePrefs", Context.MODE_PRIVATE);
        dotColor = prefs.getInt("dotColor", Color.CYAN);
    }
}
