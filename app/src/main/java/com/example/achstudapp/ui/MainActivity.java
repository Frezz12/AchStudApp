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

import com.example.achstudapp.R;
import com.example.achstudapp.adapters.AchievementAdapter;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.AchievementWrapper;
import com.example.achstudapp.models.User;

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

        viewPager = findViewById(R.id.viewPager);

        usernameView = findViewById(R.id.usernameView);
        emailView = findViewById(R.id.emailView);
        collegeView = findViewById(R.id.collegeView);
        adminView = findViewById(R.id.adminView);

        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        logoutButton = findViewById(R.id.logout);

        adminView.setVisibility(ViewPager2.GONE);

        // Проверка на null для кнопок
        if (prevButton == null) {
            Log.e("MainActivity", "prevButton не найден в макете");
            Toast.makeText(this, "Ошибка: кнопка 'Назад' не найдена", Toast.LENGTH_SHORT).show();
        }
        if (nextButton == null) {
            Log.e("MainActivity", "nextButton не найден в макете");
            Toast.makeText(this, "Ошибка: кнопка 'Вперёд' не найдена", Toast.LENGTH_SHORT).show();
        }

        api = ApiClient.getClient(token).create(ApiService.class);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getIntExtra("userId", userId);
        }

        // Настройка кнопок навигации
        if (prevButton != null) {
            prevButton.setOnClickListener(v -> {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem > 0) {
                    viewPager.setCurrentItem(currentItem - 1, true);
                }
            });
        }

        if (nextButton != null) {
            nextButton.setOnClickListener(v -> {
                int currentItem = viewPager.getCurrentItem();
                if (viewPager.getAdapter() != null && currentItem < viewPager.getAdapter().getItemCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                }
            });
        }

        // Отключение кнопок на границах
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (prevButton != null) {
                    prevButton.setEnabled(position > 0);
                }
                if (nextButton != null && viewPager.getAdapter() != null) {
                    nextButton.setEnabled(position < viewPager.getAdapter().getItemCount() - 1);
                }
            }
        });

        loadUserById(userId);

        logoutButton.setOnClickListener(v -> logout());
    }

    private void loadUserById(int id) {
        Log.d("MainActivity", "Загрузка пользователя с id: " + id);
        api.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("MainActivity", "Ответ на getUserById: code=" + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    displayUser(response.body());
                } else {
                    String errorMsg = "Ошибка получения пользователя: code=" + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ", " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("MainActivity", errorMsg);
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    if (response.code() == 401) {
                        tokenManager.clearToken();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("MainActivity", "Ошибка сети: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUser(User user) {
        usernameView.setText(user.getFirstname() + " " + user.getLastname());
        emailView.setText(user.getEmail());
        collegeView.setText(user.getCollege());
        List<AchievementWrapper> achievements = user.getAchievements();
        Log.d("MainActivity", "Достижения: " + (achievements != null ? achievements.size() : "null"));

        if (Objects.equals(user.getRole(),"admin")) {
            adminView.setVisibility(ViewPager2.VISIBLE);
        }

        if (Objects.equals(user.getRole(),"curator")) {

        }
        if (achievements == null || achievements.isEmpty()) {
            Toast.makeText(this, "Достижения отсутствуют", Toast.LENGTH_SHORT).show();
            viewPager.setAdapter(new AchievementAdapter(null));
        } else {
            viewPager.setAdapter(new AchievementAdapter(achievements));
        }
    }

    private void logout() {
        tokenManager.clearToken();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        Toast.makeText(this, "Вы успешно вышли из аккаунта", Toast.LENGTH_SHORT).show();
    }
}