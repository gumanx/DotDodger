package com.gumanx.dotdodger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CustomizeActivity extends AppCompatActivity {

    private int coins;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        // Loads stored data
        SharedPreferences prefs = getSharedPreferences("dotDodgePrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
        coins = prefs.getInt("coins", 0);

        updateCoins();
    }

    // Subtracts cost of dot from stored coins and changes the dot color
    protected void setDot(int color, int cost) {
        if (cost <= coins) {
            coins = coins - cost;
            editor.putInt("coins", coins);
            editor.putInt("dotColor", color);
            editor.apply();
            updateCoins();
        }
    }

    public void selectDefault(View view) {
        setDot(Color.CYAN, 0);
    }

    public void selectWhite(View view) {
        setDot(Color.WHITE, 10);
    }

    private void updateCoins() {
        TextView coinText = (TextView) findViewById(R.id.coinText);
        coinText.setText(Integer.toString(coins));
    }
}
