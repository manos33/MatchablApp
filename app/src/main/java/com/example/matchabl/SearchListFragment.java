package com.example.matchabl;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FieldAdapter adapter;
    private static final String TAG = "SearchListFragment";
    private JSONArray fieldsArray;
    private FavoritesManager favoritesManager;

    public SearchListFragment() {
    }

    public static SearchListFragment newInstance(JSONArray fieldsArray) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString("fieldsArray", fieldsArray.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoritesManager = new FavoritesManager(getContext());

        // Initialize the adapter with an empty list
        adapter = new FieldAdapter(new ArrayList<>(), favoritesManager);
        recyclerView.setAdapter(adapter);

        // Load fields details from server
        loadFields();

        return view;
    }

    private void loadFields() {
        if (getArguments() != null) {
            String fieldsArrayString = getArguments().getString("fieldsArray");
            try {
                fieldsArray = new JSONArray(fieldsArrayString);
                Log.d(TAG, "FieldsArray loaded: " + fieldsArray.toString());
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing fieldsArray: " + e.getMessage());
                return;
            }
        } else {
            Log.e(TAG, "Arguments are null");
            return;
        }

        List<Field> fieldList = new ArrayList<>();
        NetworkHandler networkHandler = new NetworkHandler();

        for (int i = 0; i < fieldsArray.length(); i++) {
            try {
                JSONObject fieldObject = fieldsArray.getJSONObject(i);
                int id = fieldObject.getInt("FacilityID");
                Log.d(TAG, "Field ID: " + id);
                networkHandler.getFieldDetails(getContext(), String.valueOf(id), new NetworkHandler.FieldDetailsCallback() {
                    @Override
                    public void onSuccess(JSONObject fieldDetails, JSONArray facilitySports) {
                        try {
                            JSONObject facility = fieldDetails.getJSONObject("facility");
                            JSONArray facilityInfo = facility.getJSONArray("facility_info");
                            JSONObject info = facilityInfo.getJSONObject(0);
                            String name = info.getString("FacilityName");
                            String address = info.getString("Address");
                            double rating = 4.5; // This should be replaced with the actual rating if available

                            Field field = new Field(id, name, address, rating, facilitySports);
                            fieldList.add(field);
                            Log.d(TAG, "Field added: " + name);

                            // Update adapter with loaded fields
                            adapter.updateFields(fieldList);

                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing field details: " + e.getMessage());
                        }
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
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing fieldsArray: " + e.getMessage());
            }
        }
    }
}
