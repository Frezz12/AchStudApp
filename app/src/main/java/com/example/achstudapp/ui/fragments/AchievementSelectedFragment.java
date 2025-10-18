package com.example.achstudapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.achstudapp.R;

public class AchievementSelectedFragment extends Fragment {

    int studentAchId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_ach, container, false);


        if (getArguments() != null) {
            studentAchId = getArguments().getInt("studentAchievementId", -1);
        }

        Log.d("AchievementSelectedFragment", "studentAchievementId = " + studentAchId);

        return view;
    }

}
