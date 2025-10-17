package com.example.achstudapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;

import com.example.achstudapp.R;
import com.example.achstudapp.adapters.AchievementAdapter;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.AchievementWrapper;
import com.example.achstudapp.models.User;
import com.example.achstudapp.ui.fragments.ProfileFragment;
import com.example.achstudapp.ui.fragments.SearchFragment;
import com.example.achstudapp.ui.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.*;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    TextView usernameView, emailView, collegeView, adminView;
    Button prevButton, nextButton, logoutButton;
    ApiService api;
    TokenManager tokenManager;

    BottomNavigationView bottomNavigationView;
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenManager = new TokenManager(this);
        String token = tokenManager.getToken();
        userId = tokenManager.getUserId();
        Log.d("MainActivity", "Проверка токена при запуске: " + (token != null ? token : "null") + ", userId: " + userId);

        if (token == null || token.isEmpty() || userId == -1) {
            Log.d("MainActivity", "Токен или userId отсутствует, перенаправление на LoginActivity");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("userId", tokenManager.getUserId());
        fragment.setArguments(args);
        loadFragment(fragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selected = null;

            if (item.getItemId() == R.id.nav_profile) {
                selected = new ProfileFragment();
            } else if (item.getItemId() == R.id.nav_search) {
                selected = new SearchFragment();
            } else if (item.getItemId() == R.id.nav_settings) {
                selected = new SettingsFragment();
            }

            if (selected != null) {
                loadFragment(selected);
                return true;
            }
            return false;
        });

        api = ApiClient.getClient(token).create(ApiService.class);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getIntExtra("userId", userId);
        }
    }



    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}