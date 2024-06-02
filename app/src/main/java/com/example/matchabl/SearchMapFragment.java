package com.example.matchabl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Προσθήκη listeners για τα markers και τα clicks
        mMap.setOnMarkerClickListener(this);

        // Προσθήκη markers για τα γήπεδα
        addStadiumMarkers();
    }

    private void addStadiumMarkers() {
        // Προσθήκη markers για κάθε γήπεδο
//        for (Field field : fieldList) {
//            LatLng location = new LatLng(field.getLatitude(), field.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(location).title(field.getName()));
//        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Εδώ πρέπει να υλοποιήσετε τη μετάβαση στο προφίλ του γηπέδου που αντιστοιχεί στον marker που πατήθηκε
        // Μπορείτε να χρησιμοποιήσετε το όνομα (title) του marker για να εντοπίσετε το αντίστοιχο γήπεδο στη λίστα σας
        // Και να περάσετε το αντίστοιχο ID ή άλλες πληροφορίες στο Fragment του προφίλ του γηπέδου
        return false;
    }
}
