package com.example.matchabl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdSignupActivity extends AppCompatActivity {

    private ImageView profileSetupImg, dotsImg;
    private RatingBar footballRatingBar, tennisRatingBar, basketRatingBar, volleyRatingBar;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_third_signup);

        // Initialize views
        profileSetupImg = findViewById(R.id.profileSetupImg);
        footballRatingBar = findViewById(R.id.footballRatingBar);
        tennisRatingBar = findViewById(R.id.tennisRatingBar);
        basketRatingBar = findViewById(R.id.basketRatingBar);
        volleyRatingBar = findViewById(R.id.volleyRatingBar);
        dotsImg = findViewById(R.id.dotsImg);
        finishButton = findViewById(R.id.finishButton);

        // Set click listener for finish button
        finishButton.setOnClickListener(v -> {
            int footballRating = (int)footballRatingBar.getRating();
            int tennisRating = (int)tennisRatingBar.getRating();
            int basketRating = (int)basketRatingBar.getRating();
            int volleyRating = (int)volleyRatingBar.getRating();

            // Save the ratings (this is just an example, replace it with actual saving logic)
            saveUserRatings(footballRating, tennisRating, basketRating, volleyRating);

            // Show a toast message (replace with your desired action)
            // Toast.makeText(this, "Ratings saved", Toast.LENGTH_SHORT).show();

            // Navigate to the next activity or perform any other action
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Method to save user ratings (replace with actual saving logic)
    private void saveUserRatings(int footballRating, int tennisRating, int basketRating, int volleyRating) {
        // Example: save to shared preferences or a database
        // SharedPreferences sharedPreferences = getSharedPreferences("user_ratings", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = sharedPreferences.edit();
        // editor.putInt("football_rating", footballRating);
        // editor.putInt("tennis_rating", tennisRating);
        // editor.putInt("basket_rating", basketRating);
        // editor.putInt("volley_rating", volleyRating);
        // editor.apply();
    }
}
