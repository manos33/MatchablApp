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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FieldAdapter adapter;
    private FavoritesManager favoritesManager;
    private static final String TAG = "FavoritesFragment";

    public FavoritesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoritesManager = new FavoritesManager(getContext());
        Set<String> favoriteSet = favoritesManager.getFavorites();
        List<Integer> favoriteIds = new ArrayList<>();

        // Convert from Set<String> to List<Integer>
        for (String id : favoriteSet) {
            favoriteIds.add(Integer.parseInt(id));
        }

        // Load favorite fields details from server -
        loadFavoriteFields(favoriteIds);

        return view;
    }

    private void loadFavoriteFields(List<Integer> favoriteIds) {
        if (favoriteIds.isEmpty()) {
            Log.d(TAG, "No favorites found.");
            return;
        }

        List<Field> favoriteFields = new ArrayList<>();
        NetworkHandler networkHandler = new NetworkHandler();

        for (int id : favoriteIds) {
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
                        favoriteFields.add(new Field(id, name, address, rating, facilitySports));

                        // Update adapter with loaded favorite fields
                        if (adapter == null) {
                            adapter = new FieldAdapter(favoriteFields, favoritesManager);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

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
        }
    }
}
