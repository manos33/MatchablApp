package com.example.matchabl;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

public class BookingFragment extends Fragment {

    private CalendarView calendarView;
    private LinearLayout timeSlotsContainer;

    private String sportType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        calendarView = view.findViewById(R.id.calendar_view);
        timeSlotsContainer = view.findViewById(R.id.time_slots_container);

        if (getArguments() != null) {
            sportType = getArguments().getString("sportType");
        }

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Load available times for the selected date
            loadAvailableTimes("fieldId", sportType, year, month, dayOfMonth);
        });

        return view;
    }

    private void loadAvailableTimes(String fieldId, String sportType, int year, int month, int day) {
        NetworkHandler networkHandler = new NetworkHandler();
        networkHandler.getAvailableTimes(getContext(),fieldId, sportType, year, month, day, new NetworkHandler.AvailableTimesCallback() {
            @Override
            public void onSuccess(JSONArray times) {
                timeSlotsContainer.removeAllViews();
                for (int i = 0; i < times.length(); i++) {
                    try {
                        String timeSlot = times.getString(i);
                        TextView timeSlotTextView = new TextView(getContext());
                        timeSlotTextView.setText(timeSlot);
                        timeSlotTextView.setPadding(16, 16, 16, 16);
                        timeSlotsContainer.addView(timeSlotTextView);

                        // Set click listener for each time slot
                        timeSlotTextView.setOnClickListener(v -> {
                            // Handle booking for the selected time slot
                        });

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
                // Handle error
            }


        });
    }
}
