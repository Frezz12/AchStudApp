package com.example.achstudapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.achstudapp.R;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.User;
import com.example.achstudapp.models.UserUpdateRequest;
import com.example.achstudapp.ui.LoginActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFragment extends Fragment {
    TokenManager tokenManager;
    ApiService api;
    int userId = -1;
    EditText editFirstname, editLastname, editSurname, editEmail, editCollege;
    Button updateBtn;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_user, container, false);

        editFirstname = view.findViewById(R.id.editFirstname);
        editLastname = view.findViewById(R.id.editLastname);
        editSurname = view.findViewById(R.id.editSurname);
        editEmail = view.findViewById(R.id.editEmail);
        editCollege = view.findViewById(R.id.editCollege);

        updateBtn = view.findViewById(R.id.updateBtn);

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        api = ApiClient.getClient(token).create(ApiService.class);
        userId = tokenManager.getUserId();

        loadUser();

        updateBtn.setOnClickListener(v -> updateUser());

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
                    seeActualUser(response.body());
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

    private void updateUser() {
        Log.d("UpdateFragment", "Обновления пользователя с id: " + userId);
        UserUpdateRequest req = new UserUpdateRequest(
                editFirstname.getText().toString().trim(),
                editLastname.getText().toString().trim(),
                editSurname.getText().toString().trim(),
                editEmail.getText().toString().trim(),
                editCollege.getText().toString().trim()
        );
        api.updateUser(userId, req).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SuccessUpdateUser successUpdateUser = new SuccessUpdateUser();

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, successUpdateUser)
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UpdateFragment", "Ошибка сети: " + t.getMessage());
                Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void seeActualUser(User user) {
        editFirstname.setText(user.getFirstname());
        editLastname.setText(user.getLastname());
        editSurname.setText(user.getSurname());
        editEmail.setText(user.getEmail());
        editCollege.setText(user.getCollege());
    }
}
