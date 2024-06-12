package com.example.matchabl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SportSelectionDialogFragment extends DialogFragment {

    private JSONArray facilitySports;
    private ListView sportListView;
    private Button confirmButton;

    public SportSelectionDialogFragment(JSONArray facilitySports) {
        this.facilitySports = facilitySports;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sport_selection, container, false);

        sportListView = view.findViewById(R.id.sport_list_view);
        confirmButton = view.findViewById(R.id.confirm_button);

        ArrayList<String> sportTypes = new ArrayList<>();
        final ArrayList<Integer> sportIds = new ArrayList<>(); // Add sportIds list
        for (int i = 0; i < facilitySports.length(); i++) {
            try {
                JSONObject sport = facilitySports.getJSONObject(i);
                String sportType = sport.getString("SportType");
                String sportName = sport.getString("SportName");
                int sportId = sport.getInt("SportID");
                sportTypes.add(sportName + " (" + sportType + ")");
                sportIds.add(sportId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, sportTypes);
        sportListView.setAdapter(adapter);
        sportListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        confirmButton.setOnClickListener(v -> {
            int selectedPosition = sportListView.getCheckedItemPosition();
            if (selectedPosition != ListView.INVALID_POSITION) {
                String selectedSport = sportTypes.get(selectedPosition);
                int sportId = sportIds.get(selectedPosition);
                String[] sportParts = selectedSport.split(" \\(");
                String sportName = sportParts[0];
                String sportType = sportParts[1].replace(")", "");
                ((FieldProfileFragment) getTargetFragment()).onSportTypeSelected(sportType, sportName, sportId);
                dismiss();
            }
        });

        return view;
    }
}
