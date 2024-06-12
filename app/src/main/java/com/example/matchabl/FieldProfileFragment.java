package com.example.matchabl;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldProfileFragment extends Fragment {

    private TextView fieldNameTextView;
    private TextView fieldAddressTextView;
    private TextView fieldRatingTextView;
    private LinearLayout sportsContainer;
    private LinearLayout hoursContainer;
    private Button bookButton;
    private JSONArray facilitySports;

    private static final String TAG = "FieldProfileFragment";

    // correct order
    private static final Map<String, Integer> dayOrder;
    static {
        dayOrder = new HashMap<>();
        dayOrder.put("Mon", 1);
        dayOrder.put("Tue", 2);
        dayOrder.put("Wed", 3);
        dayOrder.put("Thu", 4);
        dayOrder.put("Fri", 5);
        dayOrder.put("Sat", 6);
        dayOrder.put("Sun", 7);
    }

    public static FieldProfileFragment newInstance(String fieldId) {
        FieldProfileFragment fragment = new FieldProfileFragment();
        Bundle args = new Bundle();
        args.putString("fieldId", fieldId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fieldprof, container, false);

        fieldNameTextView = view.findViewById(R.id.field_name);
        fieldAddressTextView = view.findViewById(R.id.field_address);
        fieldRatingTextView = view.findViewById(R.id.field_rating);
        sportsContainer = view.findViewById(R.id.sports_container);
        hoursContainer = view.findViewById(R.id.hours_container);
        bookButton = view.findViewById(R.id.book_button);

        // Get field ID from arguments
        String fieldId = getArguments().getString("fieldId");
        Log.d(TAG, "Field ID: " + fieldId);

        // Load field details
        loadFieldDetails(fieldId);

        // Set book button click listener
        bookButton.setOnClickListener(v -> {
            // Show booking dialog
            showSportSelectionDialog();
        });

        return view;
    }

    private void loadFieldDetails(String fieldId) {
        NetworkHandler networkHandler = new NetworkHandler();
        networkHandler.getFieldDetails(getContext(), fieldId, new NetworkHandler.FieldDetailsCallback() {
            @Override
            public void onSuccess(JSONObject fieldDetails, JSONArray facilitySports) {
                getActivity().runOnUiThread(() -> {
                    Log.d(TAG, "Field details loaded successfully: " + fieldDetails.toString());
                    try {
                        JSONObject facility = fieldDetails.getJSONObject("facility");

                        // Facility info
                        JSONArray facilityInfo = facility.getJSONArray("facility_info");
                        JSONObject info = facilityInfo.getJSONObject(0);
                        fieldNameTextView.setText(info.getString("FacilityName"));
                        fieldAddressTextView.setText(info.getString("Address"));

                        // Facility sports
                        FieldProfileFragment.this.facilitySports = facilitySports;
                        sportsContainer.removeAllViews();
                        for (int i = 0; i < facilitySports.length(); i++) {
                            JSONObject sport = facilitySports.getJSONObject(i);
                            TextView sportTextView = new TextView(getContext());
                            sportTextView.setText(sport.getString("SportName") + " (" + sport.getString("SportType") + ") - $" + sport.getInt("price"));
                            sportTextView.setTextSize(20);
                            sportTextView.setTextColor(Color.BLACK);
                            sportTextView.setTag(sport.getInt("SportID")); // Save SportID as tag

                            sportsContainer.addView(sportTextView);
                        }

                        // Facility hours
                        JSONArray facilityHours = facility.getJSONArray("facility_hours");


                        List<JSONObject> hoursList = new ArrayList<>();
                        for (int i = 0; i < facilityHours.length(); i++) {
                            hoursList.add(facilityHours.getJSONObject(i));
                        }
                        hoursList.sort((o1, o2) -> {
                            try {
                                return Integer.compare(dayOrder.get(o1.getString("day_of_week")), dayOrder.get(o2.getString("day_of_week")));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        hoursContainer.removeAllViews();
                        for (JSONObject hour : hoursList) {
                            String dayOfWeek = hour.getString("day_of_week");
                            String opening = hour.getString("opening").split("T")[1].split("\\.")[0]; // Extract time part
                            String closing = hour.getString("closing").split("T")[1].split("\\.")[0]; // Extract time part

                            TextView hourTextView = new TextView(getContext());
                            hourTextView.setText(dayOfWeek + ": " + opening + " - " + closing);
                            hoursContainer.addView(hourTextView);
                        }

                        // Set rating (mocked for now, update with real data if available)
                        fieldRatingTextView.setText("4.5");

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing field details: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Failed to load field details: " + errorMessage);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading field details: " + error);
            }
        });
    }

    private void showSportSelectionDialog() {
        SportSelectionDialogFragment sportSelectionDialog = new SportSelectionDialogFragment(facilitySports);
        sportSelectionDialog.setTargetFragment(this, 0);
        sportSelectionDialog.show(getParentFragmentManager(), "SportSelectionDialogFragment");
    }

    public void onSportTypeSelected(String sportType, String sportName, int sportId) {
        BookingFragment bookingFragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putString("sportType", sportType);
        args.putString("sportName", sportName);
        args.putInt("sportId", sportId); // Pass SportID to BookingFragment
        args.putString("facilityId", getArguments().getString("fieldId"));
        bookingFragment.setArguments(args);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, bookingFragment)
                .addToBackStack(null)
                .commit();
    }
}
