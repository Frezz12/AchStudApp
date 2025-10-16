//package com.example.achstudapp.ui;
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.*;
//import android.content.Intent;
//
//
//import com.example.achstudapp.api.ApiClient;
//import com.example.achstudapp.api.ApiService;
//import com.example.achstudapp.api.TokenManager;
//import com.example.achstudapp.models.RegisterRequest;
//import com.example.achstudapp.models.RegisterResponse;
//
//import retrofit2.*;
//
//public class RegisterActivity extends AppCompatActivity {
//    EditText email, password, firstname, lastname, surname, college;
//    Button registerBtn;
//    ApiService api;
//    TokenManager tokenManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        email = findViewById(R.id.email);
//        password = findViewById(R.id.password);
//        firstname = findViewById(R.id.firstname);
//        lastname = findViewById(R.id.lastname);
//        surname = findViewById(R.id.surname);
//        college = findViewById(R.id.college);
//        registerBtn = findViewById(R.id.registerBtn);
//
//        tokenManager = new TokenManager(this);
//        api = ApiClient.getClient(null).create(ApiService.class);
//
//        registerBtn.setOnClickListener(v -> register());
//    }
//
//    private void register() {
//        RegisterRequest req = new RegisterRequest(
//                email.getText().toString(),
//                password.getText().toString(),
//                firstname.getText().toString(),
//                lastname.getText().toString(),
//                surname.getText().toString(),
//                college.getText().toString()
//        );
//
//        api.register(req).enqueue(new Callback<RegisterResponse>() {
//            @Override
//            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    tokenManager.saveToken(response.body().getToken());
//                    Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                    finish();
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegisterResponse> call, Throwable t) {
//                Toast.makeText(RegisterActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
