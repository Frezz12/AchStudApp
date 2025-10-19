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
import com.example.achstudapp.models.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessUpdateUser extends Fragment {
    TokenManager tokenManager;
    ApiService api;
    TextView editFirstname, editLastname, editSurname, editEmail, editCollege;
    Button backProfileBtn;
    int userId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_user, container, false);

        editFirstname = view.findViewById(R.id.editFirstname);
        editLastname = view.findViewById(R.id.editLastname);
        editSurname = view.findViewById(R.id.editSurname);
        editEmail = view.findViewById(R.id.editEmail);
        editCollege = view.findViewById(R.id.editCollege);

        backProfileBtn = view.findViewById(R.id.backProfileBtn);

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        api = ApiClient.getClient(token).create(ApiService.class);
        userId = tokenManager.getUserId();

        loadUser();

        backProfileBtn.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, profileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadUser() {
        Log.d("UpdateFragment", "Загрузка пользователя с id: " + userId);
        api.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("UpdateFragment", "Ответ на getUserById: code=" + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), "Успешно полученно", Toast.LENGTH_SHORT).show();
                    updatedDisplayUser(response.body());
                } else {
                    String errorMsg = "Ошибка получения пользователя: code=" + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ", " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("UpdateFragment", errorMsg);
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ProfileFragment", "Ошибка сети: " + t.getMessage());
                Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatedDisplayUser(User user) {
        editFirstname.setText(user.getFirstname());
        editLastname.setText(user.getLastname());
        editSurname.setText(user.getSurname());
        editEmail.setText(user.getEmail());
        editCollege.setText(user.getCollege());
    }
}
