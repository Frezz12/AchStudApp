package com.example.achstudapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achstudapp.R;
import com.example.achstudapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> allUsers; // весь список с сервера
    private List<User> filteredUsers; // список после фильтрации
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public UserAdapter(List<User> users, OnUserClickListener listener) {
        this.allUsers = new ArrayList<>(users);
        this.filteredUsers = new ArrayList<>(users);
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = filteredUsers.get(position);
        holder.name.setText(user.getFirstname() + " " + user.getLastname());
        holder.email.setText(user.getEmail());
        holder.college.setText(user.getCollege());
        holder.role.setText(user.getRole());

        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return filteredUsers.size();
    }

    public void filter(String query) {
        filteredUsers.clear();
        if (query.isEmpty()) {
            filteredUsers.addAll(allUsers);
        } else {
            query = query.toLowerCase();
            for (User user : allUsers) {
                if ((user.getFirstname() + " " + user.getLastname()).toLowerCase().contains(query) ||
                        user.getEmail().toLowerCase().contains(query)) {
                    filteredUsers.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, college, role;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.userEmail);
            college = itemView.findViewById(R.id.userCollege);
            role = itemView.findViewById(R.id.userRole);
        }
    }
}
