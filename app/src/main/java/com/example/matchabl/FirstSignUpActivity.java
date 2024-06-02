package com.example.matchabl;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FirstSignUpActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button signUpButton;
    private TextView txtError,signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupfirst);

        // Initialize the views
        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextTextPassword);
        signUpButton = findViewById(R.id.continueButton);
        txtError = findViewById(R.id.txtwrong);

        signInText = findViewById(R.id.signUpText);

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
            public void onClick(@NonNull View widget) {
                // Handle sign in click
                Intent intent = new Intent(FirstSignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull android.text.TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
                ds.setColor(Color.parseColor("#a9e978")); // Ensure color is set
            }
        };
        spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

       signInText.setText(spannableString);
       signInText.setMovementMethod(LinkMovementMethod.getInstance());



        // Set click listener for sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    txtError.setText("Username and password must be filled out.");
                    txtError.setTextColor(Color.RED);
                } else {
                    txtError.setText(""); // Clear the error message

                    // Απενεργοποίηση του κουμπιού "Continue"
                    signUpButton.setEnabled(false);



                    Context context = FirstSignUpActivity.this;
                    NetworkHandler.signUp(context, username, password, new NetworkHandler.SignUpCallback() {
                        @Override
                        public void onSuccess() {
                            // Αποθήκευση της κατάστασης εγγραφής
                            saveRegistrationState(true);
                            Intent intent = new Intent(FirstSignUpActivity.this, SignupActivity.class);
                            startActivity(intent);
                            signUpButton.setEnabled(true);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            txtError.setText(errorMessage);
                            txtError.setTextColor(Color.RED);
                            signUpButton.setEnabled(true);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            txtError.setText("An error occurred: " + errorMessage);
                            txtError.setTextColor(Color.RED);
                            signUpButton.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    private void saveRegistrationState(boolean isInProgress) {
        SharedPreferences preferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("registration_in_progress", isInProgress);
        editor.apply();
    }


    private boolean isRegistrationInProgress() {
        SharedPreferences preferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        return preferences.getBoolean("registration_in_progress", false);
    }

}
