package com.example.achstudapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achstudapp.R;
import com.example.achstudapp.models.AchievementWrapper;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
    private List<AchievementWrapper> achievements;

    public AchievementAdapter(List<AchievementWrapper> achievements) {
        this.achievements = (achievements != null) ? achievements : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ach_lite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AchievementWrapper item = achievements.get(position);
        if (item != null && item.getAchievement() != null) {
            holder.title.setText(item.getAchievement().getTitle() != null ? item.getAchievement().getTitle() : "Без названия");
//            holder.desc.setText(item.getAchievement().getDescription() != null ? item.getAchievement().getDescription() : "Без описания");
            holder.points.setText("+" + item.getAchievement().getStarPoints());
//            holder.status.setText("Статус: " + (item.getStatus() != null ? item.getStatus() : "Не указан"));
//            // Изменение цвета статуса в зависимости от значения
//            if ("Выполнено".equals(item.getStatus())) {
//                holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.EmailProfileColor));
//            } else {
//                holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
//            }
        } else {
            holder.title.setText("Нет данных");
            holder.points.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return achievements != null ? achievements.size() : 0;
    }

    public void updateAchievements(List<AchievementWrapper> newAchievements) {
        this.achievements = (newAchievements != null) ? newAchievements : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, points, status;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            points = itemView.findViewById(R.id.starPoint);
        }
    }
}