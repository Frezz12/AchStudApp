package com.example.achstudapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.achstudapp.ui.LoginActivity;

public class SettingsFragment extends Fragment {

    TokenManager tokenManager;

    ApiService api;

    int userId = -1;
    Button logoutButton, updateButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        userId = tokenManager.getUserId();
        api = ApiClient.getClient(token).create(ApiService.class);

        logoutButton = view.findViewById(R.id.logout);
        updateButton = view.findViewById(R.id.updateBtn);

        updateButton.setOnClickListener(v -> {
            UpdateFragment updateFragment = new UpdateFragment();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, updateFragment)
                    .addToBackStack(null)
                    .commit();
        });

        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        tokenManager.clearToken();
        startActivity(new Intent(requireContext(), LoginActivity.class));
        Toast.makeText(requireContext(), "Вы успешно вышли из аккаунта", Toast.LENGTH_SHORT).show();
    }
}
