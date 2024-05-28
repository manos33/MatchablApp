package com.example.matchabl;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView settings, profilepic1, wheretoplay, filterbtn, sportsimg, basketimg, footballimg, volleyimg, tennisimg, offersimg;
    TextView hellotext;
    EditText fieldSearch;
    BottomNavigationView bottomNavigationView;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.settings);
        profilepic1 = findViewById(R.id.profilepic1);
        hellotext = findViewById(R.id.hellotext);
        wheretoplay = findViewById(R.id.where);
        fieldSearch = findViewById(R.id.search_field);
        filterbtn = (ImageView) findViewById(R.id.filterimg);
        sportsimg = (ImageView) findViewById(R.id.sports);
        basketimg = (ImageView) findViewById(R.id.basketimg);
        footballimg = (ImageView) findViewById(R.id.footballimg);
        volleyimg = (ImageView) findViewById(R.id.volleyimg);
        tennisimg = (ImageView) findViewById(R.id.tennisimg);
        offersimg = (ImageView) findViewById(R.id.offersimg);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
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
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}
