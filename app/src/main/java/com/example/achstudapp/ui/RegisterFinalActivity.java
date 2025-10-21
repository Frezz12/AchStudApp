package com.example.achstudapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.achstudapp.R;
import com.example.achstudapp.api.ApiClient;
import com.example.achstudapp.api.ApiService;
import com.example.achstudapp.api.TokenManager;
import com.example.achstudapp.models.RegisterRequest;
import com.example.achstudapp.models.RegisterResponse;
import com.example.achstudapp.models.User;
import com.example.achstudapp.utils.RegisterDataManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFinalActivity extends AppCompatActivity {
    private Button btnRegister;
    private ImageButton backBtnRegF;
    private EditText editCollege;
    private RegisterDataManager dataManager;
    private ApiService api;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_final);

        btnRegister = findViewById(R.id.registerBtn);
        editCollege = findViewById(R.id.college);
        backBtnRegF = findViewById(R.id.backBtnRegF);

        dataManager = new RegisterDataManager(this);
        api = ApiClient.getClient(null).create(ApiService.class);
        tokenManager = new TokenManager(this);
//        Log.d(
//                "Имя: " + dataManager.getFirstname() + "\n" +
//                        "Фамилия: " + dataManager.getLastname() + "\n" +
//                        "Email: " + dataManager.getEmail() + "\n" +
//                        "Роль: " + dataManager.getRole() + "\n" +
//                        "Колледж: " + dataManager.getCollege()
//        );

        backBtnRegF.setOnClickListener(v -> {
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            dataManager.saveStep3(
                    editCollege.getText().toString()
            );

//            Map<String, String> body = new HashMap<>();
//            body.put("firstname", dataManager.getFirstname());
//            body.put("lastname", dataManager.getLastname());
//            body.put("surname", dataManager.getSurname());
//            body.put("email", dataManager.getEmail());
//            body.put("password", dataManager.getPassword());
//            body.put("role", dataManager.getRole());
//            body.put("college", dataManager.getCollege());
            RegisterRequest registerUserBody = new RegisterRequest(
                    dataManager.getFirstname(),
                    dataManager.getLastname(),
                    dataManager.getSurname(),
                    dataManager.getEmail(),
                    dataManager.getPassword(),
                    "student",
                    dataManager.getCollege()
            );

            api.register(registerUserBody).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("RegisterFinalActivity", response.body().toString());
                        int userId = response.body().getUser().getId();
                        String token = response.body().getToken();
                        Log.d("RegisterFinalActivity", "Получен токен: " + (token != null ? token : "null") + ", userId: " + userId);
                        tokenManager.saveToken(token, userId);
                        dataManager.clear();
                        Toast.makeText(RegisterFinalActivity.this,
                                "Добро пожаловать, " + response.body().getUser().getFirstname(),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterFinalActivity.this, MainActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        finish();
                    } else {
                        System.out.println(response.code());
                        System.out.println(response.raw());
                        Toast.makeText(RegisterFinalActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterFinalActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
