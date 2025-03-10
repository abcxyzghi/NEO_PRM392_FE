package com.example.electronics_store.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.UserResponse;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class UserManagementAdapter extends RecyclerView.Adapter<UserManagementAdapter.ViewHolder> {
    private List<UserResponse> userList;

    public UserManagementAdapter(List<UserResponse> userList) {
        this.userList = (userList != null) ? userList : new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserList(List<UserResponse> userList) {
        this.userList = (userList != null) ? userList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserResponse user = userList.get(position);
        holder.txtName.setText(user.getName() != null ? user.getName() : "N/A");
        holder.txtEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
        holder.txtRole.setText(user.getRole() != null ? user.getRole() : "N/A");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtRole;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.User_txtName);
            txtEmail = itemView.findViewById(R.id.User_txtEmail);
            txtRole = itemView.findViewById(R.id.User_txtRole);
        }
    }
}
