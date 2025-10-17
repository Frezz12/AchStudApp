package com.example.achstudapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.achstudapp.R;
import com.example.achstudapp.adapters.AchievementAdapter;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.AchievementWrapper;
import com.example.achstudapp.models.User;
import com.example.achstudapp.ui.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;



import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSelectFragments extends Fragment {
    ViewPager2 viewPager;
    TextView usernameView, emailView, collegeView, adminView;
    Button prevButton, nextButton, logoutButton;
    ApiService api;
    TokenManager tokenManager;

    BottomNavigationView bottomNavigationView;
    int userId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        viewPager = view.findViewById(R.id.viewPager);

        usernameView = view.findViewById(R.id.usernameView);
        emailView = view.findViewById(R.id.emailView);
        collegeView = view.findViewById(R.id.collegeView);
        adminView = view.findViewById(R.id.adminView);

        prevButton = view.findViewById(R.id.prevButton);
        nextButton = view.findViewById(R.id.nextButton);
        logoutButton = view.findViewById(R.id.logout);

        adminView.setVisibility(ViewPager2.GONE);

        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        api = ApiClient.getClient(token).create(ApiService.class);

        // Проверка на null для кнопок
        if (prevButton == null) {
            Log.e("MainActivity", "prevButton не найден в макете");
            Toast.makeText(requireContext(), "Ошибка: кнопка 'Назад' не найдена", Toast.LENGTH_SHORT).show();
        }
        if (nextButton == null) {
            Log.e("MainActivity", "nextButton не найден в макете");
            Toast.makeText(requireContext(), "Ошибка: кнопка 'Вперёд' не найдена", Toast.LENGTH_SHORT).show();
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

        return view;
    }

    private void loadUserById(int id) {
        Log.d("ProfileFragment", "Загрузка пользователя с id: " + id);
        api.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("ProfileFragment", "Ответ на getUserById: code=" + response.code());
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
                    Log.e("ProfileFragment", errorMsg);
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    if (response.code() == 401) {
                        tokenManager.clearToken();
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ProfileFragment", "Ошибка сети: " + t.getMessage());
                Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireContext(), "Достижения отсутствуют", Toast.LENGTH_SHORT).show();
            viewPager.setAdapter(new AchievementAdapter(null));
        } else {
            viewPager.setAdapter(new AchievementAdapter(achievements));
        }
    }

    private void logout() {
        tokenManager.clearToken();
        startActivity(new Intent(requireContext(), LoginActivity.class));
        Toast.makeText(requireContext(), "Вы успешно вышли из аккаунта", Toast.LENGTH_SHORT).show();
    }
}
