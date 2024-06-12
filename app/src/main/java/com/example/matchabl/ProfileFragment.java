package com.example.matchabl;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String SPORTS_PREF = "sports";
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;


    private LinearLayout sportsContainer;
    private TextView usernameTextView, fullnametext,sportstile;
    private CircularImageView profileImageView;
    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        sportstile = view.findViewById(R.id.imageView13);
        profileImageView = view.findViewById(R.id.imageView5);
        usernameTextView = view.findViewById(R.id.usernametext);
        fullnametext  = view.findViewById(R.id.fullnametext);
        sportsContainer = view.findViewById(R.id.sports_container);

        // Load user data from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String name = prefs.getString("name", "");
        String surname = prefs.getString("surname", "");
        String profileImagePath = prefs.getString("profile_image_path", "");

        // Log user data
        Log.d(TAG, "Username: " + username);
        Log.d(TAG, "Name: " + name);
        Log.d(TAG, "Surname: " + surname);

        // Set user data to views
        usernameTextView.setText(username);
        usernameTextView.setTextSize(15);
        fullnametext.setText(name + " "+ surname);
        fullnametext.setTextSize(20);

        // Load and set profile image if path exists
        if (!profileImagePath.isEmpty()) {
            File imageFile = new File(profileImagePath);
            if (imageFile.exists()) {
                Glide.with(this)
                        .load(imageFile)
                        .transform(new CircleCrop())
                        .into(profileImageView);
            }
        } else {
            Log.d(TAG, "No profile image found.");
        }

        // Set click listener to profile image to allow user to change it
        profileImageView.setOnClickListener(v -> {
            if (checkPermission()) {
                openGallery();
            } else {
                requestPermission();
            }
        });

        // Get the user's sports preferences
        Set<String> sportsSet = getSportsPreference();


        Log.d(TAG, "Sports preferences: " + sportsSet.toString());

        // Add sports icons to the container
        for (String sport : sportsSet) {
            ImageView sportIcon = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    500,
                    250
            );
            sportIcon.setLayoutParams(layoutParams);

            switch (sport) {
                case "Basketball":
                    sportIcon.setImageResource(R.drawable.basketimg);
                    break;
                case "Football":
                    sportIcon.setImageResource(R.drawable.footballimg);
                    break;
                case "Tennis":
                    sportIcon.setImageResource(R.drawable.tennisimg);
                    break;
                case "Volleyball":
                    sportIcon.setImageResource(R.drawable.volleyimg);
                    break;

                default:
                    Log.d(TAG, "No icon found for sport: " + sport);
                    break;
            }
            sportsContainer.addView(sportIcon);
        }

        return view;
    }


    private Set<String> getSportsPreference() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getStringSet(SPORTS_PREF, new HashSet<>());
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String imagePath = getRealPathFromURI(selectedImageUri);

            // Update profile image in SharedPreferences
            SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("profile_image_path", imagePath);
            editor.apply();

            // Upload image path to server
            uploadImagePath(imagePath);

            // Load and set profile image
            File imageFile = new File(imagePath);
            Glide.with(this)
                    .load(imageFile)
                    .transform(new CircleCrop())
                    .into(profileImageView);
        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }


    private void uploadImagePath(String imagePath) {
        NetworkHandler.uploadProfileImage(getContext(), imagePath, new NetworkHandler.UploadCallback() {
            @Override
            public void onSuccess() {

                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(String errorMessage) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(String errorMessage) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error uploading image: " + errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
