package com.example.electronics_store.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;
import com.example.electronics_store.adapter.UserAdapter;

public class UserManagementActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(UserManagementActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserManagementActivity.this, Login.class));
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        UserAdapter userAdapter = new UserAdapter(this, (userId) -> {
            Intent intent = new Intent(UserManagementActivity.this, UserDetailActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
        recyclerView.setAdapter(userAdapter);

        findViewById(R.id.fabAddUser).setOnClickListener(v -> {
            Toast.makeText(this, "Add User functionality not implemented", Toast.LENGTH_SHORT).show();
        });
    }
}