package com.example.matchabl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class SecondSignupActivity extends AppCompatActivity {

    private ToggleButton footballButton, tennisButton, volleyballButton, basketballButton;
    private Button continueButton;
    private TextView signUpText;

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
        signUpText = findViewById(R.id.signUpText);

        // Set up the "SIGN IN" text with color and click functionality
        String text = "Already have an account? SIGN IN";
        SpannableString spannableString = new SpannableString(text);

        // Define the color for the "SIGN IN" text
        int start = text.indexOf("SIGN IN");
        int end = start + "SIGN IN".length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#a9e978"));
        spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make "SIGN IN" clickable without underline
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle sign in click
                Intent intent = new Intent(SecondSignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(android.text.TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
                ds.setColor(Color.parseColor("#a9e978")); // Ensure color is set
            }
        };
        spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        signUpText.setText(spannableString);
        signUpText.setMovementMethod(LinkMovementMethod.getInstance());

        // Set up toggle buttons with rounded corners and custom colors
        setupToggleButton(footballButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));
        setupToggleButton(tennisButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));
        setupToggleButton(volleyballButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));
        setupToggleButton(basketballButton, Color.parseColor("#C5F0A4"), Color.parseColor("#F4F4F4"));

        // Handle continue button click
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the next activity
                Intent intent = new Intent(SecondSignupActivity.this, ThirdSignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupToggleButton(final ToggleButton button, final int selectedColor, final int unselectedColor) {
        button.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                button.setBackgroundColor(selectedColor);
                // Perform action when button is checked
                switch (button.getId()) {
                    case R.id.footballButton:
                        // Handle football button press
                        break;
                    case R.id.tennisButton:
                        // Handle tennis button press
                        break;
                    case R.id.volleyballButton:
                        // Handle volleyball button press
                        break;
                    case R.id.basketballButton:
                        // Handle basketball button press
                        break;
                    default:
                        break;
                }
            } else {
                button.setBackgroundColor(unselectedColor);
                // Perform action when button is unchecked
            }
        });
    }
}
