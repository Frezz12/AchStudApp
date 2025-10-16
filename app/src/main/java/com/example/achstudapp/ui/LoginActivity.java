package com.example.achstudapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.content.Intent;

import com.example.achstudapp.R;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.LoginRequest;
import com.example.achstudapp.models.LoginResponse;

import java.io.IOException;

import retrofit2.*;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button loginBtn, registerBtn;
    ApiService api;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        tokenManager = new TokenManager(this);
        api = ApiClient.getClient(null).create(ApiService.class);

        loginBtn.setOnClickListener(v -> login());

        registerBtn.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterStep1Activity.class)));
    }

    private void login() {
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        if (emailStr.isEmpty() || passwordStr.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("LoginActivity", "Отправка запроса на авторизацию: email=" + emailStr);
        LoginRequest request = new LoginRequest(emailStr, passwordStr);
        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("LoginActivity", "Ответ от сервера: code=" + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    int userId = response.body().getUser().getId();
                    Log.d("LoginActivity", "Получен токен: " + (token != null ? token : "null") + ", userId: " + userId);
                    tokenManager.saveToken(token, userId);
                    Toast.makeText(LoginActivity.this,
                            "Добро пожаловать, " + response.body().getUser().getFirstname(),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = "Ошибка входа: code=" + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ", " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("LoginActivity", errorMsg);
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "Ошибка сети: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}