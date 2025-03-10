package com.example.electronics_store.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;
import com.example.electronics_store.adapter.UserAdapter;
import com.example.electronics_store.adapter.UserManagementAdapter;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.RetrofitClient;
import com.example.electronics_store.retrofit.UserResponse;
import com.google.android.material.navigation.NavigationView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private UserManagementAdapter userAdapter;
    private DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermanagement);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Danh Sách Người Dùng");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        userAdapter = new UserManagementAdapter(new ArrayList<>());
        recyclerView.setAdapter(userAdapter);

        fetchUsers();
    }
    private void fetchUsers() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<UserResponse>> call = apiService.getAllUsers();

        call.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserResponse>> call, @NonNull Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userAdapter.setUserList(response.body());
                } else {
                    Toast.makeText(UserManagementActivity.this, "Lỗi khi tải danh sách người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserResponse>> call, @NonNull Throwable t) {
                Toast.makeText(UserManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, AdminActivity.class));
        } else if (id == R.id.nav_users) {
            startActivity(new Intent(this, UserManagementActivity.class));
        } else if (id == R.id.nav_products) {
            startActivity(new Intent(this, ProductManagementActivity.class));
        } else if (id == R.id.nav_categories) {
            startActivity(new Intent(this, CategoryManagementActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}