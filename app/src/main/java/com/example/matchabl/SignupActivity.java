package com.example.matchabl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, surnameInput, emailInput, phoneInput, ageInput;
    private Button continueButton;
    private TextView txtWrong;

    // Έλεγχος κατάστασης στο επόμενο Activity πριν από τη μετάβαση πίσω


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize the views
        nameInput = findViewById(R.id.nameInput);
        surnameInput = findViewById(R.id.surnameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        ageInput = findViewById(R.id.ageInput);
        continueButton = findViewById(R.id.continueButton);
        txtWrong = findViewById(R.id.txtwrong);

        // Set click listener for continue button to go to the next step of sign up
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String surname = surnameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String ageStr = ageInput.getText().toString().trim();




                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || phone.isEmpty() || ageStr.isEmpty()) {
                    txtWrong.setText("All fields must be filled out.");
                    txtWrong.setTextColor(Color.RED);
                } else {

                    // Disable continue button to prevent multiple clicks
                    continueButton.setEnabled(false);

                    int age = Integer.parseInt(ageStr);
                    txtWrong.setText(""); // Clear the error message

                    Context context = SignupActivity.this;
                    NetworkHandler.createProfile(context, name, surname, email, phone, age, new NetworkHandler.SignUpCallback() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SignupActivity.this, SecondSignupActivity.class);
                            startActivity(intent);

                            // Re-enable the continue button
                            continueButton.setEnabled(true);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            txtWrong.setText(errorMessage);
                            txtWrong.setTextColor(Color.RED);
                            // Re-enable the continue button
                            continueButton.setEnabled(true);

                        }

                        @Override
                        public void onError(String errorMessage) {
                            txtWrong.setText("An error occurred: " + errorMessage);
                            txtWrong.setTextColor(Color.RED);
                            // Re-enable the continue button
                            continueButton.setEnabled(true);

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Εάν η κατάσταση εγγραφής είναι true, αποτρέψτε τη μετάβαση πίσω
        if (isRegistrationInProgress()) {
            Toast.makeText(this, "Registration in progress. Please complete the process.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }


    private boolean isRegistrationInProgress() {
        SharedPreferences preferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        return preferences.getBoolean("registration_in_progress", false);
    }


}
