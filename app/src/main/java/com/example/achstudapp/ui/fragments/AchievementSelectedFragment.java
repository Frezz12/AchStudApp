package com.example.achstudapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.achstudapp.R;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.AchievementItem;
import com.example.achstudapp.models.StudentAchievementResponce;

import java.io.IOException;

import retrofit2.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class AchievementSelectedFragment extends Fragment {

    Button deleteBtn;
    TokenManager tokenManager;
    ApiService api;

    int studentAchId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_ach, container, false);

        deleteBtn = view.findViewById(R.id.delete);

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        api = ApiClient.getClient(token).create(ApiService.class);

        if (getArguments() != null) {
            studentAchId = getArguments().getInt("studentAchievementId", -1);
        }

        deleteBtn.setOnClickListener(v -> deleteStudAch());

        Log.d("AchievementSelectedFragment", "studentAchievementId = " + studentAchId);
        loadStudAch();
        return view;
    }

    private void loadStudAch() {
        Log.d("loadStudAch", "Загрузка достижения с id: " + studentAchId);
        api.getStudAchById(studentAchId).enqueue(new Callback<StudentAchievementResponce>() {

            @Override
            public void onResponse(Call<StudentAchievementResponce> call, Response<StudentAchievementResponce> response) {
                Log.d("loadStudAch", "Ответ на loadStudAch: code=" + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    AchievementItem item = response.body().getAchievementItem();
                    displayAch(item);
                } else {
                    String errorMsg = "Ошибка получения пользователя: code=" + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ", " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("loadStudAch", errorMsg);
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentAchievementResponce> call, Throwable t) {
                Log.e("loadStudAch", "Ошибка сети: " + t.getMessage());
                Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteStudAch() {
        Log.d("deleteStudAch", "Загрузка достижения с id: " + studentAchId);
        api.deleteStudAch(studentAchId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Достижение успешно удалено: ", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("deleteStudAch", "Ошибка сети: " + t.getMessage());
                Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAch(AchievementItem item) {

    }
}
