package com.example.achstudapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achstudapp.R;
import com.example.achstudapp.adapters.UserAllAchievementsApdapter;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.AchievementItem;
import com.example.achstudapp.models.StudentAchievementResponce;
import com.example.achstudapp.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllAchFragment extends Fragment {
    private RecyclerView recyclerView;
    TokenManager tokenManager;
    ApiService api;
    private List<StudentAchievementResponce> studentAchievementResponces;
    private UserAllAchievementsApdapter adapter;
    private int userId = -1;


    Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_ach, container, false);

        backButton = view.findViewById(R.id.backBtn);

        recyclerView = view.findViewById(R.id.recyclerView);

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        api = ApiClient.getClient(token).create(ApiService.class);
        userId = tokenManager.getUserId();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        loadAllAch(userId);

        return view;
    }

    private void loadAllAch(int userId) {
        Log.d("ViewAllAchFragment", "Загрузка достижений студента с id: " + userId);
        api.getAllMyAchievement(userId, null, null).enqueue(new Callback<List<StudentAchievementResponce>>() {
            public void onResponse(Call<List<StudentAchievementResponce>> call, Response<List<StudentAchievementResponce>> response) {
                Log.d("ViewAllAchFragment", "Ответ на getAllMyAchievement: code=" + response.code());
                if (response.isSuccessful() && response.body() != null){
                    studentAchievementResponces = response.body();
                    adapter = new UserAllAchievementsApdapter(studentAchievementResponces, studentAchievementResponce -> {
                        Bundle bundle = new Bundle();
                        bundle.putInt("studentAchievementId", studentAchievementResponce.getId());

                        AchievementSelectedFragment achievementSelectedFragment = new AchievementSelectedFragment();
                        achievementSelectedFragment.setArguments(bundle);

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, achievementSelectedFragment)
                                .addToBackStack(null)
                                .commit();
                    });
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StudentAchievementResponce>> call, Throwable t) {
                Log.d("ViewAllAchFragment" , t.getMessage());
                Toast.makeText(requireContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
