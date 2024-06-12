package com.example.matchabl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen duration in milliseconds
    private static final long SPLASH_SCREEN_DELAY = 3000; // 3 seconds
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String AUTH_KEY = "Authorization";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);



// Code to test that if nothing was saved in the preferences the code would work correctly

//        SharedPreferences preferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();
//
//        SharedPreferences apreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor1 = apreferences.edit(); // Χρησιμοποιήστε το "apreferences"
//        editor1.clear(); // Χρησιμοποιήστε το "editor1"
//        editor1.apply(); // Χρησιμοποιήστε το "editor1.apply()"
//
//        SharedPreferences bpreferences = getSharedPreferences("sports", MODE_PRIVATE);
//        SharedPreferences.Editor editor2 = apreferences.edit(); // Χρησιμοποιήστε το "apreferences"
//        editor2.clear(); // Χρησιμοποιήστε το "editor1"
//        editor2.apply(); // Χρησιμοποιήστε το "editor1.apply()"




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.matchablgreen)); // Use your desired color
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        // Delay for a few seconds and then check the Authorization token
        new Handler().postDelayed(() -> {
            // Check if the Authorization token is valid
            if (isAuthTokenValid()) {
                // Start the main activity
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
            } else {
                // Start the login/signup activity
                Intent loginIntent = new Intent(SplashScreenActivity.this, LSActivity.class);
                startActivity(loginIntent);
            }
            finish();
        }, SPLASH_SCREEN_DELAY);
    }

    // Method to check if the Authorization token is valid - wanted to execute this check with the server
    private boolean isAuthTokenValid() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);
        return !TextUtils.isEmpty(authToken);
    }
}
