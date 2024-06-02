package com.example.matchabl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class SearchDialogFragment extends DialogFragment {

    private String selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private String selectedSport;

    private SearchDialogListener listener;

    public interface SearchDialogListener {
        void onSearch(String date, String startTime, String endTime, String sport);
    }

    public void setSearchDialogListener(SearchDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search, container, false);

        MaterialButton dateButton = view.findViewById(R.id.dateButton);
        TimePicker startTimePicker = view.findViewById(R.id.startTimePicker);
        TimePicker endTimePicker = view.findViewById(R.id.endTimePicker);
        RadioGroup sportRadioGroup = view.findViewById(R.id.sportRadioGroup);
        MaterialButton dialogSearchButton = view.findViewById(R.id.dialog_search_button);

        // Initialize DatePicker and TimePickers
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        // Create DatePicker
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Date");
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker<Long> datePicker = builder.build();

        dateButton.setOnClickListener(v -> datePicker.show(getParentFragmentManager(), "DATE_PICKER"));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = datePicker.getHeaderText();
            dateButton.setText(selectedDate);
        });

        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        // Set minute to 0 programmatically
        startTimePicker.setCurrentMinute(0);
        endTimePicker.setCurrentMinute(0);

        startTimePicker.setOnTimeChangedListener((view12, hourOfDay, minute) -> {
            startTimePicker.setCurrentMinute(0);
            selectedStartTime = hourOfDay + ":00";
            if (hourOfDay >= endTimePicker.getCurrentHour()) {
                endTimePicker.setCurrentHour(hourOfDay + 1);
            }
        });

        endTimePicker.setOnTimeChangedListener((view13, hourOfDay, minute) -> {
            endTimePicker.setCurrentMinute(0);
            selectedEndTime = hourOfDay + ":00";
            if (hourOfDay <= startTimePicker.getCurrentHour()) {
                startTimePicker.setCurrentHour(hourOfDay - 1);
            }
        });

        sportRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioFootball) {
                selectedSport = "football";
            } else if (checkedId == R.id.radioBasketball) {
                selectedSport = "basketball";
            } else if (checkedId == R.id.radioTennis) {
                selectedSport = "tennis";
            } else if (checkedId == R.id.radioVolleyball) {
                selectedSport = "volleyball";
            }
        });

        dialogSearchButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSearch(selectedDate, selectedStartTime, selectedEndTime, selectedSport);
            } else {
                Toast.makeText(getContext(), "No listener attached", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        return view;
    }
}
