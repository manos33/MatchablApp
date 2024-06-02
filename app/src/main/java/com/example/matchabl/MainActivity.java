package com.example.matchabl;

import static com.example.matchabl.NetworkHandler.getFieldsBySportAndPage;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity implements SearchDialogFragment.SearchDialogListener {

    ImageView settings, profilepic1, wheretoplay, filterbtn, sportsimg, basketimg, footballimg, volleyimg, tennisimg, offersimg;
    Button searchButton;
    TextView hellotext;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.settings);
        profilepic1 = findViewById(R.id.profilepic1);
        hellotext = findViewById(R.id.hellotext);
        wheretoplay = findViewById(R.id.where);
        searchButton = findViewById(R.id.searchButton);
        sportsimg = findViewById(R.id.sports);
        basketimg = findViewById(R.id.basketimg);
        footballimg = findViewById(R.id.footballimg);
        volleyimg = findViewById(R.id.volleyimg);
        tennisimg = findViewById(R.id.tennisimg);
        offersimg = findViewById(R.id.offersimg);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        footballimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFieldsBySportAndPage(MainActivity.this, "football", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                    }
                });
            }
        });

        basketimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFieldsBySportAndPage(MainActivity.this, "basketball", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                    }
                });
            }
        });

        tennisimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFieldsBySportAndPage(MainActivity.this, "tennis", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                    }
                });
            }
        });

        volleyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFieldsBySportAndPage(MainActivity.this, "volleyball", 1, new NetworkHandler.GetFieldsCallback() {
                    @Override
                    public void onSuccess(JSONArray fieldsArray) {
                        navigateToSearchListFragment(fieldsArray);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                    }
                });
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        showHomeLayout();
                        return true;
                    case R.id.navigation_favorites:
                        selectedFragment = new FavoritesFragment();
                        break;
                    case R.id.navigation_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.navigation_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();

                    // Κρύψε το home layout περιεχόμενο
                    findViewById(R.id.settings).setVisibility(View.GONE);
                    findViewById(R.id.profilepic1).setVisibility(View.GONE);
                    findViewById(R.id.hellotext).setVisibility(View.GONE);
                    findViewById(R.id.where).setVisibility(View.GONE);
                    findViewById(R.id.searchButton).setVisibility(View.GONE);
                    findViewById(R.id.sports).setVisibility(View.GONE);
                    findViewById(R.id.basketimg).setVisibility(View.GONE);
                    findViewById(R.id.footballimg).setVisibility(View.GONE);
                    findViewById(R.id.volleyimg).setVisibility(View.GONE);
                    findViewById(R.id.tennisimg).setVisibility(View.GONE);
                    findViewById(R.id.offersimg).setVisibility(View.GONE);

                    // Δείξε το fragment container
                    findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void showHomeLayout() {
        // Κρύψε το fragment container
        findViewById(R.id.fragment_container).setVisibility(View.GONE);

        // Δείξε το home layout περιεχόμενο
        findViewById(R.id.settings).setVisibility(View.VISIBLE);
        findViewById(R.id.profilepic1).setVisibility(View.VISIBLE);
        findViewById(R.id.hellotext).setVisibility(View.VISIBLE);
        findViewById(R.id.where).setVisibility(View.VISIBLE);
        findViewById(R.id.searchButton).setVisibility(View.VISIBLE);
        findViewById(R.id.sports).setVisibility(View.VISIBLE);
        findViewById(R.id.basketimg).setVisibility(View.VISIBLE);
        findViewById(R.id.footballimg).setVisibility(View.VISIBLE);
        findViewById(R.id.volleyimg).setVisibility(View.VISIBLE);
        findViewById(R.id.tennisimg).setVisibility(View.VISIBLE);
        findViewById(R.id.offersimg).setVisibility(View.VISIBLE);
    }

    private void navigateToSearchListFragment(JSONArray fieldsArray) {
        SearchListFragment fragment = SearchListFragment.newInstance(fieldsArray);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Κρύψε το home layout περιεχόμενο
        findViewById(R.id.settings).setVisibility(View.GONE);
        findViewById(R.id.profilepic1).setVisibility(View.GONE);
        findViewById(R.id.hellotext).setVisibility(View.GONE);
        findViewById(R.id.where).setVisibility(View.GONE);
        findViewById(R.id.searchButton).setVisibility(View.GONE);
        findViewById(R.id.sports).setVisibility(View.GONE);
        findViewById(R.id.basketimg).setVisibility(View.GONE);
        findViewById(R.id.footballimg).setVisibility(View.GONE);
        findViewById(R.id.volleyimg).setVisibility(View.GONE);
        findViewById(R.id.tennisimg).setVisibility(View.GONE);
        findViewById(R.id.offersimg).setVisibility(View.GONE);

        // Δείξε το fragment container
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearch(String date, @Nullable String startTime, @Nullable String endTime, String sport) {
        NetworkHandler.getAvailableFacilities(this, sport, date, startTime, endTime, new NetworkHandler.GetFieldsCallback() {
            @Override
            public void onSuccess(JSONArray fieldsArray) {
                navigateToSearchListFragment(fieldsArray);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
    }
}
