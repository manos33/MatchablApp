//package com.example.matchabl;
//
//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.widget.DatePicker;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.view.ContextThemeWrapper;
//
//import java.util.Calendar;
//
//public class CustomDatePickerDialog extends DatePickerDialog {
//
//    public CustomDatePickerDialog(@NonNull Context context, @Nullable DatePickerDialog.OnDateSetListener listener, int year, int month, int dayOfMonth) {
//        super(new ContextThemeWrapper(context, R.style.CustomDatePickerDialogTheme), listener, year, month, dayOfMonth);
//        // Customize your dialog here
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // Change background color
//        setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", this);
//        setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", this);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Additional customizations can be done here if needed
//        DatePicker datePicker = getDatePicker();
//        Calendar calendar = Calendar.getInstance();
//        datePicker.setMinDate(calendar.getTimeInMillis()); // Disable previous dates
//    }
//}
