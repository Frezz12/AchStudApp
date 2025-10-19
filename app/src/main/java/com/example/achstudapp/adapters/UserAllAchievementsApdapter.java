package com.example.achstudapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achstudapp.R;
import com.example.achstudapp.models.AchievementItem;
import com.example.achstudapp.models.StudentAchievementResponce;
import com.example.achstudapp.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserAllAchievementsApdapter extends RecyclerView.Adapter<UserAllAchievementsApdapter.UserAllAchievementsViewHolder> {

    private List<StudentAchievementResponce> allMyAchievements;
    private UserAllAchievementsApdapter.OnAchClickListener listener;

    public interface OnAchClickListener {
        void onUserClick(StudentAchievementResponce studentAchievementResponce);
    }

    public UserAllAchievementsApdapter(List<StudentAchievementResponce> allMyAchievements, UserAllAchievementsApdapter.OnAchClickListener listener) {
        this.allMyAchievements = new ArrayList<>(allMyAchievements);
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserAllAchievementsApdapter.UserAllAchievementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievements, parent, false);
        return new UserAllAchievementsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAllAchievementsApdapter.UserAllAchievementsViewHolder holder, int position) {
        StudentAchievementResponce studentAchievementResponce = allMyAchievements.get(position);
        Log.d("onBindViewHolder", studentAchievementResponce.getUser().getFirstname());

        AchievementItem achievementItem = new AchievementItem(
                studentAchievementResponce.getAchievementItem().getTitle(),
                studentAchievementResponce.getAchievementItem().getDescription(),
                studentAchievementResponce.getAchievementItem().getStarPoints()
        );
        holder.title.setText(achievementItem.getTitle());
        holder.desc.setText(achievementItem.getDescription());
        holder.star.setText(String.valueOf(achievementItem.getStarPoints()));

        holder.itemView.setOnClickListener(v -> listener.onUserClick(studentAchievementResponce));
    }

    @Override
    public int getItemCount() {
        return allMyAchievements.size();
    }

    static class UserAllAchievementsViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, star;
        public UserAllAchievementsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            star = itemView.findViewById(R.id.starPoint);
        }
    }
}
