package com.example.matchabl;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Set;

public class ProfileFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SPORTS_PREF = "sports_preference";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get the sports container LinearLayout
        LinearLayout sportsContainer = view.findViewById(R.id.sports_container);

        // Get the user's sports preferences
        Set<String> sportsSet = getSportsPreference();

        // Add sports icons to the container
        // Add sports icons to the container
        for (String sport : sportsSet) {
            ImageView sportIcon = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    400, // Width in pixels
                     200// Height in pixels
            );
            sportIcon.setLayoutParams(layoutParams);
            // Set the appropriate drawable based on the sport
            switch (sport) {
                case "Football":
                    sportIcon.setImageResource(R.drawable.footballimg);
                    break;
                case "Tennis":
                    sportIcon.setImageResource(R.drawable.tennisimg);
                    break;
                case "Volleyball":
                    sportIcon.setImageResource(R.drawable.volleyimg);
                    break;
                case "Basketball":
                    sportIcon.setImageResource(R.drawable.basketimg);
                    break;
                // Add cases for other sports
            }
            sportsContainer.addView(sportIcon);
        }



        return view;
    }

    // Method to get sports preferences from SharedPreferences
    private Set<String> getSportsPreference() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        return prefs.getStringSet(SPORTS_PREF, new HashSet<>());
    }
}
