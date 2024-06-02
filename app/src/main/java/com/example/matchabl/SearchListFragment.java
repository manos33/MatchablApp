package com.example.matchabl;

import android.os.Bundle;
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

    private static final String ARG_FIELDS = "fields";
    private JSONArray fieldsArray;

    public static SearchListFragment newInstance(JSONArray fieldsArray) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FIELDS, fieldsArray.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                fieldsArray = new JSONArray(getArguments().getString(ARG_FIELDS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Field> fieldList = new ArrayList<>();

        if (fieldsArray != null) {
            for (int i = 0; i < fieldsArray.length(); i++) {
                try {
                    JSONObject facility = fieldsArray.getJSONObject(i);
                    int id = facility.getInt("FacilityID");
                    String name = facility.getString("FacilityName");
                    String address = facility.getString("Address");
                    double rating = facility.getDouble("rating");

                    fieldList.add(new Field(id, name, address, rating));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        FieldAdapter adapter = new FieldAdapter(fieldList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
