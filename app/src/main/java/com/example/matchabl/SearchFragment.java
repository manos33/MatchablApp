package com.example.matchabl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SearchFragment extends Fragment {

    private RadioGroup toggleSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        toggleSwitch = view.findViewById(R.id.toggleSwitch);

        // Set initial fragment
        if (toggleSwitch.getCheckedRadioButtonId() == R.id.radio_map) {
            showMapFragment();
        } else {
            showListFragment();
        }

        toggleSwitch.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_map) {
                showMapFragment();
            } else {
                showListFragment();
            }
        });

        return view;
    }

    private void showListFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchListFragment());
        transaction.commit();
    }

    private void showMapFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchMapFragment());
        transaction.commit();
    }
}
