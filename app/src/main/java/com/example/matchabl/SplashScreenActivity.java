package com.example.matchabl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen duration in milliseconds
    private static final long SPLASH_SCREEN_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen); // Replace with the layout file for your splash screen

        // Delay for a few seconds and then start the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent mainIntent = new Intent(SplashScreenActivity.this, LSActivity.class);
                startActivity(mainIntent);
                finish(); // Close the splash screen activity to prevent it from being shown again when pressing back
            }
        }, SPLASH_SCREEN_DELAY);
    }
}
