package com.example.achstudapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.achstudapp.R;
import com.example.achstudapp.utils.RegisterDataManager;

public class RegisterStep1Activity extends AppCompatActivity {
    private EditText editFirstname, editLastname, editSurname;
    private Button btnNext;
    private ImageButton backBtnReg1;
    private RegisterDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);

        editFirstname = findViewById(R.id.firstname);
        editLastname = findViewById(R.id.lastname);
        editSurname = findViewById(R.id.surname);
        btnNext = findViewById(R.id.nextBtn);
        backBtnReg1 = findViewById(R.id.backBtnReg1);

        dataManager = new RegisterDataManager(this);

        backBtnReg1.setOnClickListener(v -> {
            finish();
        });

        btnNext.setOnClickListener(v -> {
            dataManager.saveStep1(
                    editFirstname.getText().toString(),
                    editLastname.getText().toString(),
                    editSurname.getText().toString()
            );
            startActivity(new Intent(this, RegisterStep2Activity.class));
        });
    }
}
