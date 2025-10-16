package com.example.achstudapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.achstudapp.R;
import com.example.achstudapp.utils.RegisterDataManager;

public class RegisterStep2Activity extends AppCompatActivity {
    private EditText editEmail, editPassword;
    private Button btnNext;
    private RegisterDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        btnNext = findViewById(R.id.nextBtn2);

        dataManager = new RegisterDataManager(this);

        btnNext.setOnClickListener(v -> {
            dataManager.saveStep2(
                    editEmail.getText().toString(),
                    editPassword.getText().toString()
            );
            startActivity(new Intent(this, RegisterFinalActivity.class));
        });
    }
}
