package com.example.matchabl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Προσθήκη της κλάσης Log για καταγραφή
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingFragment extends Fragment {

    private CalendarView calendarView;
    private LinearLayout timeSlotsContainer;
    private Button bookButton;

    private String sportType;
    private String sportName;
    private String facilityId;
    private int sportId;
    private String selectedDate;

    private List<CheckBox> selectedSlots = new ArrayList<>();

    private static final String TAG = "BookingFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        calendarView = view.findViewById(R.id.calendar_view);
        timeSlotsContainer = view.findViewById(R.id.time_slots_container);
        bookButton = view.findViewById(R.id.book_button);

        if (getArguments() != null) {
            sportType = getArguments().getString("sportType");
            sportName = getArguments().getString("sportName");
            facilityId = getArguments().getString("facilityId");
            sportId = getArguments().getInt("sportId");
        }

        // Set min date to today
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        // Αποθήκευση της αρχικής ημερομηνίας ως σήμερα
        Calendar today = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(today.getTime());

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Format the selected date
            Calendar selectedCalDate = Calendar.getInstance();
            selectedCalDate.set(year, month, dayOfMonth);
            selectedDate = dateFormat.format(selectedCalDate.getTime());

            // Load available times for the selected date
            loadAvailableTimes(facilityId, sportName, sportType, selectedDate);
        });

        if (bookButton != null) {
            bookButton.setOnClickListener(v -> {
                List<String> selectedTimeSlots = new ArrayList<>();
                for (CheckBox checkBox : selectedSlots) {
                    if (checkBox.isChecked()) {
                        selectedTimeSlots.add(checkBox.getText().toString());
                    }
                }
                if (!selectedTimeSlots.isEmpty()) {
                    String startTime = selectedTimeSlots.get(0).split(" - ")[0];
                    String endTime = selectedTimeSlots.get(selectedTimeSlots.size() - 1).split(" - ")[1];

                    // Καταγραφή των δεδομένων πριν την αποστολή
                    Log.d(TAG, "Facility ID: " + facilityId);
                    Log.d(TAG, "Sport ID: " + sportId);
                    Log.d(TAG, "Date: " + selectedDate);
                    Log.d(TAG, "Start Time: " + startTime);
                    Log.d(TAG, "End Time: " + endTime);

                    NetworkHandler networkHandler = new NetworkHandler();
                    networkHandler.bookTimeSlots(getContext(), Integer.parseInt(facilityId), sportId, selectedDate, startTime, endTime, new NetworkHandler.BookingCallback() {
                        @Override
                        public void onSuccess() {

                            Toast.makeText(getContext(), "Booking successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(getContext(), "Booking failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(getContext(), "Booking error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        return view;
    }

    private void loadAvailableTimes(String fieldId, String sportName, String sportType, String date) {
        NetworkHandler networkHandler = new NetworkHandler();
        networkHandler.getAvailableTimes(getContext(), fieldId, sportName, sportType, date, new NetworkHandler.AvailableTimesCallback() {
            @Override
            public void onSuccess(JSONArray availability) {
                timeSlotsContainer.removeAllViews();
                selectedSlots.clear();
                for (int i = 0; i < availability.length(); i++) {
                    try {
                        JSONObject field = availability.getJSONObject(i);
                        String fieldName = field.getString("field_name");
                        JSONArray availableHours = field.getJSONArray("available_hours");

                        TextView fieldNameTextView = new TextView(getContext());
                        fieldNameTextView.setText(fieldName);
                        fieldNameTextView.setTextSize(18);
                        fieldNameTextView.setPadding(8, 8, 8, 8);
                        timeSlotsContainer.addView(fieldNameTextView);

                        for (int j = 0; j < availableHours.length(); j++) {
                            JSONObject hour = availableHours.getJSONObject(j);
                            String from = hour.getString("from");
                            String to = hour.getString("to");
                            String timeSlot = from + " - " + to;

                            CheckBox timeSlotCheckBox = new CheckBox(getContext());
                            timeSlotCheckBox.setText(timeSlot);
                            timeSlotsContainer.addView(timeSlotCheckBox);
                            selectedSlots.add(timeSlotCheckBox);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
