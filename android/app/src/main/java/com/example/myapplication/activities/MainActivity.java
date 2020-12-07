package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.example.myapplication.fragments.SearchFragment;
import com.example.myapplication.models.User;
import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static Fragment lastFragment;
    private static int lastItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = SessionManager.fetchToken(this);
        if (token == null) {
            startLoginActivity();
            return;
        }
        checkUserToken(token);

        if (lastFragment == null) {
            lastFragment = new HomeFragment();
            lastItemId = R.id.home_item;
        }
        loadFragment(lastFragment);

        configureBottomNavigation();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void checkUserToken(String token) {
        NetworkService.getInstance().getJSONApi().getUserByToken(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("ERROR", Objects.requireNonNull(t.getMessage()));
            }
        });
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