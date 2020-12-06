package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.example.myapplication.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static Fragment lastFragment;
    private static int lastItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (lastFragment == null) {
            lastFragment = new HomeFragment();
            lastItemId = R.id.home_item;
        }
        loadFragment(lastFragment);

        configureBottomNavigation();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            lastFragment = fragment;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void configureBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.nav_bar);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.home_item:
                if (lastItemId != R.id.home_item) {
                    fragment = new HomeFragment();
                }
                break;
            case R.id.search_item:
                if (lastItemId != R.id.search_item) {
                    fragment = new SearchFragment();
                }
                break;
            case R.id.profile_item:
                if (lastItemId != R.id.profile_item) {
                    fragment = new ProfileFragment();
                }
        }
        lastItemId = item.getItemId();
        return loadFragment(fragment);
    }
}