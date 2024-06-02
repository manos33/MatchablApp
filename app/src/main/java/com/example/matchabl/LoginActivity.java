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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    ImageView loginimg;
    TextView forgotPasswordText, signUpTextView, txtWrong;
    EditText usernameInput, passwordInput;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginimg = (ImageView) findViewById(R.id.loginimg);
        forgotPasswordText = (TextView) findViewById(R.id.forgotPasswordText);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        loginButton = (Button) findViewById(R.id.loginButton);
        signUpTextView = (TextView) findViewById(R.id.signUpText);
        txtWrong = (TextView) findViewById(R.id.txtWrong); // Assuming there's a TextView to show errors

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
                ds.setColor(Color.parseColor("#a9e978")); // Ensure color is set
            }
        };
        spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        signUpTextView.setText(spannableString);
        signUpTextView.setMovementMethod(LinkMovementMethod.getInstance());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    txtWrong.setText("All fields must be filled out.");
                    txtWrong.setTextColor(Color.RED);
                } else {
                    NetworkHandler.login(LoginActivity.this, username, password, new NetworkHandler.SignUpCallback() {
                        @Override
                        public void onSuccess() {
                            // Handle successful login
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Handle login failure
                            txtWrong.setText(errorMessage);
                            txtWrong.setTextColor(Color.RED);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Handle error during the request
                            txtWrong.setText("An error occurred: " + errorMessage);
                            txtWrong.setTextColor(Color.RED);
                        }
                    });
                }
            }
        });
    }
}
