package com.example.achstudapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.achstudapp.R;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.AchievementItem;
import com.example.achstudapp.models.AchievementsItemRequest;
import com.example.achstudapp.models.GrandToStudentRequest;
import com.example.achstudapp.models.StudentAchievementResponce;
import com.example.achstudapp.ui.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAchFragment extends Fragment {

    EditText editTitle, editDesc, editStar, studId;
    Button createBtn;
    ApiService api;
    TokenManager tokenManager;

    int userId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_ach, container, false);

        editTitle = view.findViewById(R.id.editTitle);
        editDesc = view.findViewById(R.id.editDesc);
        editStar = view.findViewById(R.id.editStar);
        studId = view.findViewById(R.id.studId);

        createBtn = view.findViewById(R.id.createBtn);

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        api = ApiClient.getClient(token).create(ApiService.class);


        createBtn.setOnClickListener(v -> create());

        return view;
    }

    private void create() {
        String titleStr = editTitle.getText().toString().trim();
        String descStr = editDesc.getText().toString().trim();
        String starStr = editStar.getText().toString().trim();
        String userIdStr = studId.getText().toString();
        userId = Integer.parseInt(userIdStr);

        if (titleStr.isEmpty()) {
            Toast.makeText(requireContext(), "Введите название", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descStr.isEmpty()) {
            Toast.makeText(requireContext(), "Введите название", Toast.LENGTH_SHORT).show();
            return;
        }
        if (starStr.isEmpty()) {
            Toast.makeText(requireContext(), "Введите количество звезд", Toast.LENGTH_SHORT).show();
            return;
        }

        int value = 0;


        if (!starStr.isEmpty()) {
            try {
                value = Integer.parseInt(starStr);
                Log.d("CreateAchFragment", "Введено число: " + value);
            } catch (NumberFormatException e) {
                Log.e("CreateAchFragment", "Ошибка: введено не число");
            }
        } else {
            Log.e("MainActivity", "Поле пустое");
        }

        Log.d("CreateAchFragment", "Отправка запроса на создание достижения");
        AchievementsItemRequest req = new AchievementsItemRequest(
                titleStr,
                descStr,
                value
        );

        api.createAchievement(req).enqueue(new Callback<AchievementItem>() {
            @Override
            public void onResponse(Call<AchievementItem> call, Response<AchievementItem> response) {
                Log.d("CreateAchFragment", "Ответ от сервера: code=" + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    GrandToStudentRequest reqGrand = new GrandToStudentRequest(
                            response.body().getId()
                    );
                    api.grandToStudent(userId, reqGrand).enqueue(new Callback<StudentAchievementResponce>() {
                        @Override
                        public void onResponse(Call<StudentAchievementResponce> call, Response<StudentAchievementResponce> response) {

                            ViewAllAchFragment viewAllAchFragment = new ViewAllAchFragment();

                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, viewAllAchFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }

                        @Override
                        public void onFailure(Call<StudentAchievementResponce> call, Throwable t) {
                            Log.e("grandToStudent", "Ошибка сети: " + t.getMessage());
                            Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<AchievementItem> call, Throwable t) {
                Log.e("CreateAchFragment", "Ошибка сети: " + t.getMessage());
                Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
