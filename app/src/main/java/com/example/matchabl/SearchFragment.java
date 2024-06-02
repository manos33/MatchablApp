package com.example.matchabl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



// SearchFragment.java
public class SearchFragment extends Fragment {

    private Switch viewSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        viewSwitch = view.findViewById(R.id.viewSwitch);

        // Set initial fragment
        if (viewSwitch.isChecked()) {
            showMapFragment();
        } else {
            showListFragment();
        }

        viewSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
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
