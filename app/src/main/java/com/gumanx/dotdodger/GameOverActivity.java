package com.gumanx.dotdodger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Gets score from passed in intent and displays it
        int score = getIntent().getIntExtra("score", 0);
        TextView scoreIntText = (TextView) findViewById(R.id.scoreIntText);
        scoreIntText.setText(Integer.toString(score));

        // Compares score against stored high score
        // If the score is larger, set it as the new high score
        SharedPreferences prefs = getSharedPreferences("dotDodgePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("coins", score);
        if (prefs.getInt("highScore", 0) < score) {
            TextView scoreText = (TextView) findViewById(R.id.scoreText);
            scoreText.setText("New High Score:");

            prefsEditor.putInt("highScore", score);
        }
        prefsEditor.apply();
    }

    public void replay(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void toMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
