package com.example.matchabl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SPORTS_PREF = "sports_preference";

    ImageView loginimg;
    TextView forgotPasswordText, signUpTextView, txtWrong;
    EditText usernameInput, passwordInput;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginimg = findViewById(R.id.loginimg);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpText);
        txtWrong = findViewById(R.id.txtWrong);

        String text = "Don't have an account? SIGN UP";
        SpannableString spannableString = new SpannableString(text);

        // Define the color for the "SIGN UP" text
        int start = text.indexOf("SIGN UP");
        int end = start + "SIGN UP".length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#a9e978"));
        spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make "SIGN UP" clickable without underline
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Handle sign up click
                Intent intent = new Intent(LoginActivity.this, FirstSignUpActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull android.text.TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
                ds.setColor(Color.parseColor("#a9e978"));
            }
        };
        spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        signUpTextView.setText(spannableString);
        signUpTextView.setMovementMethod(LinkMovementMethod.getInstance());

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                txtWrong.setText("All fields must be filled out.");
                txtWrong.setTextColor(Color.RED);
            } else {
                NetworkHandler.login(LoginActivity.this, username, password, new NetworkHandler.SignUpCallback() {
                    @Override
                    public void onSuccess() {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                        txtWrong.setText(errorMessage);
                        txtWrong.setTextColor(Color.RED);
                    }

                    @Override
                    public void onError(String errorMessage) {

                        txtWrong.setText(/*"An error occurred: " + errorMessage*/"No connection with server.");
                        txtWrong.setTextColor(Color.RED);
                    }
                });
            }
        });
    }
}
