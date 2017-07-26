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
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        // Loads stored data
        prefs = getSharedPreferences("dotDodgePrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
        coins = prefs.getInt("coins", 0);

        updateCoins();
    }

    // Subtracts cost of dot from stored coins and changes the dot color
    private void setDot(int color, int cost) {
        if (!prefs.getBoolean("bought" + color, false) && cost <= coins) {
            coins = coins - cost;
            editor.putInt("coins", coins);
            editor.putBoolean("bought" + color, true);
            updateCoins();
        }
        if (prefs.getBoolean("bought" + color, false)) {
            editor.putInt("dotColor", color);
        }
        editor.apply();
    }

    public void selectCyan(View view) {
        setDot(Color.CYAN, 0);
    }

    public void selectWhite(View view) {
        setDot(Color.WHITE, 100);
    }

    // Refreshes the coin display
    private void updateCoins() {
        TextView coinText = (TextView) findViewById(R.id.coinText);
        coinText.setText(Integer.toString(coins));
    }
}
