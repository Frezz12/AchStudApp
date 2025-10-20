package com.example.achstudapp.ui.fragments;

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
import com.example.achstudapp.models.ChangeRoleUserForAdminRequest;
import com.example.achstudapp.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeRoleUserForAdminFragment extends Fragment {
    EditText editId, editNewRole;
    Button updateBtn;
    private ApiService api;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_role_user, container, false);

        tokenManager = new TokenManager(requireContext());
        String token = tokenManager.getToken();
        api = ApiClient.getClient(token).create(ApiService.class);

        editId = view.findViewById(R.id.editId);
        editNewRole = view.findViewById(R.id.editNewRole);

        updateBtn = view.findViewById(R.id.changeRoleBtn);

        updateBtn.setOnClickListener(v -> change());

        return view;
    }

    private void change() {
        String userChangeIdStr = editId.getText().toString().trim();
        int userChangeId = Integer.parseInt(userChangeIdStr);
        ChangeRoleUserForAdminRequest req = new ChangeRoleUserForAdminRequest(
                editNewRole.getText().toString().trim()
        );

        Log.d("change", "Изменение роли пользователя с id: " + userChangeId);
        api.updateRoleUserForAdmin(userChangeId, req).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileFragment profileFragment = new ProfileFragment();

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, profileFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ChangeRoleNetwork", "Ошибка сети: " + t.getMessage());
                Toast.makeText(requireContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
