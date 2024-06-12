package com.example.matchabl;

import static com.example.matchabl.NetworkHandler.getAvailableFacilities;
import static com.example.matchabl.NetworkHandler.getFieldsBySportAndPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchDialogFragment.SearchDialogListener {

    ImageView settings, profilepic1, basketimg, footballimg, volleyimg, tennisimg, offersimg;
    Button searchButton;
    TextView hellotext, wheretoplay, rectext, sportsimg;
    BottomNavigationView bottomNavigationView;
    RecyclerView recommendedRecyclerView;
    FieldAdapter adapter;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profilepic1 = findViewById(R.id.profilepic1);
        hellotext = findViewById(R.id.hellotext);
        wheretoplay = findViewById(R.id.where);
        searchButton = findViewById(R.id.searchButton);
        sportsimg = findViewById(R.id.sports);
        basketimg = findViewById(R.id.basketimg);
        footballimg = findViewById(R.id.footballimg);
        volleyimg = findViewById(R.id.volleyimg);
        tennisimg = findViewById(R.id.tennisimg);
        rectext = findViewById(R.id.rectext);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        recommendedRecyclerView = findViewById(R.id.recommended_recycler_view);

        // Setup RecyclerView
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FieldAdapter(new ArrayList<>(), new FavoritesManager(this));
        recommendedRecyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "");
        hellotext.setText("Hello " + name + "!");

        loadRecommendedFields();

        footballimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Football image clicked");
                getFieldsBySportAndPage(MainActivity.this, "football", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        Log.d(TAG, "Football fields fetched successfully");
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Failed to fetch football fields: " + errorMessage);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e(TAG, "Error fetching football fields: " + errorMessage);
                    }
                });
            }
        });

        basketimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Basketball image clicked");
                getFieldsBySportAndPage(MainActivity.this, "basketball", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        Log.d(TAG, "Basketball fields fetched successfully");
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Failed to fetch basketball fields: " + errorMessage);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e(TAG, "Error fetching basketball fields: " + errorMessage);
                    }
                });
            }
        });

        tennisimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Tennis image clicked");
                getFieldsBySportAndPage(MainActivity.this, "tennis", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        Log.d(TAG, "Tennis fields fetched successfully");
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Failed to fetch tennis fields: " + errorMessage);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e(TAG, "Error fetching tennis fields: " + errorMessage);
                    }
                });
            }
        });

        volleyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Volleyball image clicked");
                getFieldsBySportAndPage(MainActivity.this, "volleyball", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        Log.d(TAG, "Volleyball fields fetched successfully");
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Failed to fetch volleyball fields: " + errorMessage);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e(TAG, "Error fetching volleyball fields: " + errorMessage);
                    }
                });
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Search button clicked");
                SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
                searchDialogFragment.setSearchDialogListener(MainActivity.this);
                searchDialogFragment.show(getSupportFragmentManager(), "searchDialog");
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Log.d(TAG, "Home navigation item selected");
                        showHomeLayout();
                        return true;
                    case R.id.navigation_favorites:
                        Log.d(TAG, "Favorites navigation item selected");
                        selectedFragment = new FavoritesFragment();
                        break;
                    case R.id.navigation_profile:
                        Log.d(TAG, "Profile navigation item selected");
                        selectedFragment = new ProfileFragment();
                        break;
                }
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();

                    hideHomeLayout();
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void loadRecommendedFields() {
        NetworkHandler networkHandler = new NetworkHandler();
        networkHandler.getRecommendedFields(this, new NetworkHandler.RecommendedFieldsCallback() {
            @Override
            public void onSuccess(JSONArray recommendedFieldsArray) {
                Log.d(TAG, "Recommended fields fetched successfully: " + recommendedFieldsArray.toString());
                List<Field> fieldList = new ArrayList<>();
                for (int i = 0; i < recommendedFieldsArray.length(); i++) {
                    try {
                        JSONObject fieldObject = recommendedFieldsArray.getJSONObject(i);
                        int id = fieldObject.getInt("FacilityID");
                        String name = fieldObject.getString("FacilityName");
                        String address = fieldObject.getString("Address");
                        double rating = fieldObject.getDouble("rating");

                        // Create a Field object and add it to the list
                        Field field = new Field(id, name, address, rating, null);
                        fieldList.add(field);
                        Log.d(TAG, "Field added: " + name);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing recommendedFieldsArray: " + e.getMessage());
                    }
                }
                // Update adapter with loaded fields
                runOnUiThread(() -> {
                    adapter.updateFields(fieldList);
                    Log.d(TAG, "Updated adapter with recommended fields: " + fieldList.size());
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Failed to load recommended fields: " + errorMessage);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading recommended fields: " + error);
            }
        });
    }


    private void showHomeLayout() {
        Log.d(TAG, "Showing home layout");
        findViewById(R.id.fragment_container).setVisibility(View.GONE);

        findViewById(R.id.profilepic1).setVisibility(View.VISIBLE);
        findViewById(R.id.hellotext).setVisibility(View.VISIBLE);
        findViewById(R.id.where).setVisibility(View.VISIBLE);
        findViewById(R.id.searchButton).setVisibility(View.VISIBLE);
        findViewById(R.id.sports).setVisibility(View.VISIBLE);
        findViewById(R.id.basketimg).setVisibility(View.VISIBLE);
        findViewById(R.id.footballimg).setVisibility(View.VISIBLE);
        findViewById(R.id.volleyimg).setVisibility(View.VISIBLE);
        findViewById(R.id.tennisimg).setVisibility(View.VISIBLE);
        findViewById(R.id.rectext).setVisibility(View.VISIBLE);
        findViewById(R.id.recommended_recycler_view).setVisibility(View.VISIBLE);
    }

    private void hideHomeLayout() {
        Log.d(TAG, "Hiding home layout");

        findViewById(R.id.profilepic1).setVisibility(View.GONE);
        findViewById(R.id.hellotext).setVisibility(View.GONE);
        findViewById(R.id.where).setVisibility(View.GONE);
        findViewById(R.id.searchButton).setVisibility(View.GONE);
        findViewById(R.id.sports).setVisibility(View.GONE);
        findViewById(R.id.basketimg).setVisibility(View.GONE);
        findViewById(R.id.footballimg).setVisibility(View.GONE);
        findViewById(R.id.volleyimg).setVisibility(View.GONE);
        findViewById(R.id.tennisimg).setVisibility(View.GONE);
        findViewById(R.id.rectext).setVisibility(View.GONE);
        findViewById(R.id.recommended_recycler_view).setVisibility(View.GONE);
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }

    private void navigateToSearchListFragment(JSONArray fieldsArray) {
        Log.d(TAG, "Navigating to SearchListFragment with fieldsArray: " + fieldsArray.toString());
        SearchListFragment fragment = SearchListFragment.newInstance(fieldsArray);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        hideHomeLayout();
    }

    @Override
    public void onSearch(String date, String startTime, String endTime, String sport) {
        Log.d(TAG, "Search button clicked with date: " + date + ", startTime: " + startTime + ", endTime: " + endTime + ", sport: " + sport);
        getAvailableFacilities(this, sport, date, startTime, endTime, new NetworkHandler.GetFieldsCallback() {
            @Override
            public void onSuccess(JSONArray fieldsArray) {
                Log.d(TAG, "Facilities fetched successfully: " + fieldsArray.toString());
                navigateToSearchListFragment(fieldsArray);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Failed to fetch facilities: " + errorMessage);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Error fetching facilities: " + errorMessage);
            }
        });
    }
}
