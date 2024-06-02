package com.example.matchabl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

public class SecondSignupActivity extends AppCompatActivity {

    private ToggleButton footballButton, tennisButton, volleyballButton, basketballButton;
    private Button continueButton;
    private TextView signUpText, txtWrong;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SPORTS_PREF = "sports_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second_signup);

        // Initialize views
        footballButton = findViewById(R.id.footballButton);
        tennisButton = findViewById(R.id.tennisButton);
        volleyballButton = findViewById(R.id.volleyballButton);
        basketballButton = findViewById(R.id.basketballButton);
        continueButton = findViewById(R.id.continueButton2);
        //signUpText = findViewById(R.id.signUpText);
        txtWrong = findViewById(R.id.txtWrong);



        // Set up toggle buttons with rounded corners and custom colors
        setupToggleButton(footballButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));
        setupToggleButton(tennisButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));
        setupToggleButton(volleyballButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));
        setupToggleButton(basketballButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));

        // Handle continue button click
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable continue button to prevent multiple clicks
                continueButton.setEnabled(false);

                // Save selected sports preferences
                saveSportsPreferences();
            }
        });
    }

    private void setupToggleButton(final ToggleButton button, final int selectedColor, final int unselectedColor) {
        button.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                button.setBackgroundColor(selectedColor);
            } else {
                button.setBackgroundColor(unselectedColor);
            }
        });
    }

    private void saveSportsPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> sportsSet = new HashSet<>();
        if (footballButton.isChecked()) sportsSet.add("Football");
        if (tennisButton.isChecked()) sportsSet.add("Tennis");
        if (volleyballButton.isChecked()) sportsSet.add("Volleyball");
        if (basketballButton.isChecked()) sportsSet.add("Basketball");

        editor.putStringSet(SPORTS_PREF, sportsSet);
        editor.apply();

        // Send sports preferences to the server
        NetworkHandler.sendSportsPreferences(this, sportsSet, new NetworkHandler.SignUpCallback() {
            @Override
            public void onSuccess() {
                // Navigate to the next activity on success
                Intent intent = new Intent(SecondSignupActivity.this, ThirdSignupActivity.class);
                startActivity(intent);
                // Re-enable the continue button
                continueButton.setEnabled(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Show error message on failure
                txtWrong.setText(errorMessage);
                txtWrong.setTextColor(Color.RED);
                // Re-enable the continue button
                continueButton.setEnabled(true);
            }

            @Override
            public void onError(String errorMessage) {
                // Show error message on error
                txtWrong.setText("An error occurred: " + errorMessage);
                txtWrong.setTextColor(Color.RED);
                // Re-enable the continue button
                continueButton.setEnabled(true);
            }
        });
    }
}
